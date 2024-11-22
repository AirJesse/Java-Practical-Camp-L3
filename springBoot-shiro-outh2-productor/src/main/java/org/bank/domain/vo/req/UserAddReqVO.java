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
public class UserAddReqVO {

    /**
     * 用户名
     */
    @NotBlank(message = "账号不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 创建来源(1.web 2.android 3.ios )
     */
    private Integer createWhere;

    /**
     * 所属机构
     */
    @NotBlank(message = "所属机构不能为空")
    private String deptId;

    /**
     * 性别(1.男 2.女)
     */
    private String sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 所拥有的角色
     */
    private List<String> roleIds;
}
