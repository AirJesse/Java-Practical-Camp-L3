package org.bank.domain.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName: RolePermissionOperationReqVO
 * TODO:类文件简单描述

 */
@Data
public class RolePermissionOperationReqVO {

    /**
     * 角色id
     */
    @NotBlank(message = "角色id不能为空")
    private String roleId;

    /**
     * 菜单权限集合
     */
    @NotEmpty(message = "菜单权限集合不能为空")
    private List<String> permissionIds;
}
