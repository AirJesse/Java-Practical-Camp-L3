package org.bank.domain.vo.req;

/**
 * @author zhaoaiping
 * @date 2022/8/24 16:42
 */

public class BankMoneyAddReqVo  {

    private String username;
    private Double money;

    public BankMoneyAddReqVo(String username, Double money) {
        this.username = username;
        this.money = money;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
