package org.bank.domain.vo.req;

import lombok.Data;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.req
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class UpdatePasswordReqVO {
    /**
     * 旧密码
     */
    private String oldPwd;

    /**
     * 新密码
     */
    private String newPwd;
}
