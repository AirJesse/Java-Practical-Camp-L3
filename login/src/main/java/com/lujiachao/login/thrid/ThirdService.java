package com.lujiachao.login.thrid;

import com.lujiachao.login.thrid.dto.BankMoney;

public interface ThirdService {
    public BankMoney getMyMoney(String accessToken);


    String getBankAccessToken(String code, int userId);
}
