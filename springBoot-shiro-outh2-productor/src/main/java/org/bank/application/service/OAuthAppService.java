package org.bank.application.service;

import org.bank.common.constant.Constant;
import org.bank.domain.entity.OuthClientDetails;
import org.bank.domain.service.OAuthService;
import org.bank.domain.service.OuthClientDetailsService;
import org.bank.domain.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OAuthAppService implements OAuthService {
    private long accessTokenTimeOut = 1000 * 60 * 60 * 24 * 10L;


    @Autowired
    private RedisService redisService;

    @Autowired
    private OuthClientDetailsService clientService;


    @Override
    public void addAuthCode(String authCode, String userId) {
        redisService.set(Constant.OUTH_CODE_KEY + authCode, userId, accessTokenTimeOut, TimeUnit.MILLISECONDS);
    }

    @Override
    public void addAccessToken(String accessToken, String userId) {
        redisService.set(Constant.OUTH_ACCESSTOKEN_KEY + accessToken, userId, accessTokenTimeOut, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getUserIdByAuthCode(String authCode) {
        return (String) redisService.get(Constant.OUTH_CODE_KEY + authCode);
    }

    @Override
    public String getUserIdByAccessToken(String accessToken) {
        return (String) redisService.get(Constant.OUTH_ACCESSTOKEN_KEY + accessToken);
    }

    /**
     * 错误的名字，应该返回userId
     *
     * @param authCode
     * @return
     */
    @Override
    @Deprecated
    public String getUsernameByAuthCode(String authCode) {
        return (String) redisService.get(Constant.OUTH_CODE_KEY + authCode);
    }

    /**
     * 错误的名字，应该返回userId
     *
     * @param accessToken
     * @return
     */
    @Override
    @Deprecated
    public String getUsernameByAccessToken(String accessToken) {
        return (String) redisService.get(Constant.OUTH_ACCESSTOKEN_KEY + accessToken);
    }

    @Override
    public long getExpireIn() {
        return accessTokenTimeOut;
    }

    @Override
    public boolean checkAuthCode(String authCode) {
        return redisService.get(Constant.OUTH_CODE_KEY + authCode) != null;
    }

    @Override
    public boolean checkAccessToken(String accessToken) {
        return redisService.get(Constant.OUTH_ACCESSTOKEN_KEY + accessToken) != null;
    }

    @Override
    public OuthClientDetails checkClientId(String clientId) {
        return clientService.getClientForClientId(clientId);
    }


}