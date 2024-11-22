package org.bank.domain.service;


import org.bank.domain.entity.OuthClientDetails;

public interface OAuthService {
    //添加 auth code
    public void addAuthCode(String authCode, String userId);
    //添加 access token
    public void addAccessToken(String accessToken, String userId);

    //验证auth code是否有效
    boolean checkAuthCode(String authCode);
    //验证access token是否有效
    boolean checkAccessToken(String accessToken);

    String getUserIdByAuthCode(String authCode);

    String getUserIdByAccessToken(String accessToken);

    String getUsernameByAuthCode(String authCode);
    String getUsernameByAccessToken(String accessToken);


    //auth code / access token 过期时间
    long getExpireIn();


    public OuthClientDetails checkClientId(String clientId);

}
