package org.bank.domain.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.req
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class PermissionUpdateReqVO {
    /**
     *id
     */
    @NotBlank(message = "id 不能为空")
    private String id;

    /**
     *状态1:正常 0：禁用
     */
    private Integer status;

    /**
     *菜单权限名称
     */
    @NotBlank(message = "菜单权限名称不能为空")
    private String name;

    /**
     *菜单权限标识，shiro 适配restful
     */
    private String perms;

    /**
     *接口地址
     */
    private String url;

    /**
     *请求方式 和url 配合使用 (我们用 路径匹配的方式做权限管理的时候用到)
     */
    private String method;

    /**
     *父级id
     */
    @NotNull(message = "所属菜单不能为空")
    private String pid;

    /**
     *排序码
     */
    private Integer orderNum;

    /**
     *菜单权限类型(1:目录;2:菜单;3:按钮)
     */
    @NotNull(message = "菜单权限类型不能为空")
    private Integer type;

    /**
     *vue专用  菜单视图路径组成
     */
    private String path;


    /**
     *vue专用  菜单视图标
     */
    private String icon;

    /**
     *vue专用  菜单视图路径
     */
    private String component;

}
