package org.bank.domain.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.req
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class UserUpdateReqVO {

    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 所属机构
     */
    private String deptId;

    /**
     * 真实名称
     */
    private String realName;


    /**
     * 邮箱
     */
    private String email;

    /**
     * 账户状态(1.正常 2.锁定 )
     */
    private Integer status;

    /**
     * 性别(1.男 2.女)
     */
    private Integer sex;

    /**
     * 密码
     */
    private String password;

    /**
     * 所拥有的角色
     */
    private List<String> roleIds;

}
