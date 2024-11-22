package org.bank.common.shiro;


import com.alibaba.druid.util.StringUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.bank.common.constant.Constant;
import org.bank.common.util.JwtTokenUtil;
import org.bank.domain.service.PermissionService;
import org.bank.domain.service.RedisService;
import org.bank.domain.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.config
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    @Lazy
    private RedisService redisService;
    @Autowired
    @Lazy
    private PermissionService permissionService;
    @Autowired
    @Lazy
    private RoleService roleService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof CustomPasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        String accessToken=(String)SecurityUtils.getSubject().getPrincipal();
        String userId= JwtTokenUtil.getUserId(accessToken);;
        boolean flag=false;
        if(StringUtils.isEmpty(userId)&&redisService.hasKey(Constant.OUTH_ACCESSTOKEN_KEY+accessToken)){
            flag=true;
            userId=redisService.get(Constant.OUTH_ACCESSTOKEN_KEY+accessToken).toString();
        }
        if(StringUtils.isEmpty(userId)){
            return authorizationInfo;
        }
        if(redisService.hasKey(Constant.JWT_REFRESH_KEY+userId)&&redisService.getExpire(Constant.JWT_REFRESH_KEY+userId, TimeUnit.MILLISECONDS)>JwtTokenUtil.getRemainingTime(accessToken)){
            List<String> roleNames=roleService.getRoleNames(userId);
            if(roleNames!=null&&!roleNames.isEmpty()){
                authorizationInfo.addRoles(roleService.getRoleNames(userId));
            }
            authorizationInfo.setStringPermissions(permissionService.getPermissionsByUserId(userId));
        }else {
            if(flag){
                List<String> roleNames=roleService.getRoleNames(userId);
                if(roleNames!=null&&!roleNames.isEmpty()){
                    authorizationInfo.addRoles(roleService.getRoleNames(userId));
                }
                authorizationInfo.setStringPermissions(permissionService.getPermissionsByUserId(userId));
            }else{
                Claims claimsFromToken = JwtTokenUtil.getClaimsFromToken(accessToken);
                if(claimsFromToken.get(Constant.JWT_ROLES_KEY)!=null){
                    authorizationInfo.addRoles((Collection<String>) claimsFromToken.get(Constant.JWT_ROLES_KEY));
                }
                if(claimsFromToken.get(Constant.JWT_PERMISSIONS_KEY)!=null){
                    authorizationInfo.addStringPermissions((Collection<String>) claimsFromToken.get(Constant.JWT_PERMISSIONS_KEY));
                }

            }

        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomPasswordToken token= (CustomPasswordToken) authenticationToken;
        SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo(token.getPrincipal(),token.getPrincipal(),getName());
        return simpleAuthenticationInfo;
    }
}
