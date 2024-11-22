package org.bank.domain.entity;

import lombok.Data;

/**
 * @author zhaoaiping
 * @date 2022/8/24 16:42
 */
@Data
public class BankMoney  {
    private String id;
    private String username;
    private Double money;

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
