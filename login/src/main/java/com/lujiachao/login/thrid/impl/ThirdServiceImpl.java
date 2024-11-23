package com.lujiachao.login.thrid.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lujiachao.login.common.Constant;
import com.lujiachao.login.thrid.ThirdService;
import com.lujiachao.login.thrid.dto.BankMoney;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ThirdServiceImpl implements ThirdService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${bank.api.url}")
    private String bankApiUrl;

    @Override
    public BankMoney getMyMoney(String accessToken) {
        String url = bankApiUrl + "/bank/myMoney";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("access_token", accessToken);

        // Create entity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send request with headers
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JSONObject jsonObject = JSONUtil.parseObj(response.getBody());
        if (!jsonObject.get("code").equals(0)) {
            throw new RuntimeException("获取银行信息失败:" + jsonObject.get("msg"));
        }
        JSONObject data = jsonObject.getJSONObject("data");
        BankMoney bankMoney = new BankMoney();
        bankMoney.setBalance(data.getDouble("money"));
        bankMoney.setUsername(data.getStr("userName"));
        return bankMoney;

    }

    @Override
    public String getBankAccessToken(String code, int userId) {
        String url = bankApiUrl + "/outh/accessTokenByCode?code=" + code;
        String responseJson = restTemplate.getForObject(url, String.class);
        String accessToken = JSONUtil.parseObj(responseJson).getStr("access_token");
        if (accessToken == null) {
            throw new RuntimeException("获取accessToken失败");
        }
        redisTemplate.opsForValue().set(Constant.BANK_ACCESS_TOKEN_KEY + userId, accessToken);
        return accessToken;
    }
}
