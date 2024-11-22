package org.bank.common.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.shiro
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
public class CustomPasswordToken  extends UsernamePasswordToken {
    private String token;

    private String reqType;
    public CustomPasswordToken(String token,String type) {
        this.token = token;
        reqType=type;
    }

    public String getReqType(){
        return reqType;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }



}
