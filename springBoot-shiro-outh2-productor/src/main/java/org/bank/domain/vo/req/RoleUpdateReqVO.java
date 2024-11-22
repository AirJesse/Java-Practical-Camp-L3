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
public class RoleUpdateReqVO {

    /**
     * 角色id
     */
    @NotBlank(message = "角色 id 不能为空")
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 状态(1:正常0:弃用)
     */
    private Integer status;

    /**
     * 所拥有的菜单权限
     */
    private List<String> permissions;
}
