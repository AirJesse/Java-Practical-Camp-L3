package org.bank.domain.vo.resp;

import lombok.Data;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.resp
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class UserInfoRespVO {

    /**
     * 用户id
     */
    private String id;

    /**
     * 账号
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;


    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 所属机构id
     */
    private String deptId;

    /**
     * 所属机构名称
     */
    private String deptName;

}
