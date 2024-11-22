package org.bank.common.shiro;


import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.bank.common.constant.Constant;
import org.bank.common.constant.ReqType;
import org.bank.common.exception.BusinessException;
import org.bank.common.exception.code.BaseResponseCode;
import org.bank.common.util.JwtTokenUtil;
import org.bank.domain.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.shiro
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
public class CustomHashedCredentialsMatcher extends SimpleCredentialsMatcher {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        CustomPasswordToken customPasswordToken = (CustomPasswordToken) token;
        //判断当前用户登录放式属于系统登录还是Oauth2.0第三方系统登录
        //系统登录
        if (customPasswordToken.getReqType().equals(ReqType.SYSTEM.toString())) {
            String accessToken = (String) customPasswordToken.getPrincipal();
            String userId = JwtTokenUtil.getUserId(accessToken);
            if (redisService.hasKey(Constant.ACCOUNT_LOCK_KEY + userId)) {
                throw new BusinessException(BaseResponseCode.ACCOUNT_LOCK_ERROR);
            }
            if (redisService.hasKey(Constant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken)) {
                throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
            }
            if (redisService.hasKey(Constant.JWT_REFRESH_STATUS + accessToken)) {
                return true;
            }
            if (JwtTokenUtil.isTokenExpired(accessToken)) {
                throw new BusinessException(BaseResponseCode.TOKEN_PAST_DUE);
            }
            if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId) && redisService.getExpire(Constant.JWT_REFRESH_KEY + userId, TimeUnit.MILLISECONDS) > JwtTokenUtil.getRemainingTime(accessToken)) {
                if (!redisService.hasKey(Constant.JWT_REFRESH_IDENTIFICATION + accessToken)) {
                    throw new BusinessException(BaseResponseCode.TOKEN_PAST_DUE);
                }
            }
            return true;
        }else {  //oauth2.0 授权码校验
            String accessToken = (String) customPasswordToken.getPrincipal();
            String userId = "";
            if (redisService.hasKey(Constant.OUTH_ACCESSTOKEN_KEY + accessToken)) {
                userId = redisService.get(Constant.OUTH_ACCESSTOKEN_KEY + accessToken).toString();
            }
            if (StringUtils.isEmpty(userId)) {
                return false;
            }
            if (redisService.hasKey(Constant.ACCOUNT_LOCK_KEY + userId)) {
                throw new BusinessException(BaseResponseCode.ACCOUNT_LOCK_ERROR);
            }
            return true;
        }

    }
}
