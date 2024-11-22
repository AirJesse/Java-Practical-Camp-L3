package org.bank.domain.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.req
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class LoginReqVO {

    /**
     *账号
     */
    @NotBlank(message = "账号不能为空")
    private String username;

    /**
     *用户密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     *登录类型(1:pc;2:App)
     */
    @NotBlank(message = "登录类型不能为空")
    private String type;
}
