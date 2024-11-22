package org.bank.domain.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.resp
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class PermissionRespNode {
    /**
     * id
     */
    private String id;

    /**
     * 菜单权限名称
     */
    private String title;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织名称
     */
    private String label;

    /**
     * 菜单权限标识，shiro 适配restful
     */
    private String perms;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求方式 和url 配合使用 (我们用 路径匹配的方式做权限管理的时候用到)
     */
    private String method;

    /**
     * 父级id
     */
    private String pid;

    /**
     * 父级名称
     */
    private String pidName;

    /**
     * 菜单权限类型(1:目录;2:菜单;3:按钮)
     */
    private Integer type;

    /**
     * 编码(前后端分离 前段对按钮显示隐藏控制 btn-permission-search 代表 菜单权限管理的列表查询按钮)
     */
    private String code;

    /**
     * 排序码
     */
    private Integer orderNum;

    /**
     * 是否展开 默认不展开(false)
     */
    private boolean spread=true;

    /**
     * 是否选中 默认false
     */
    private boolean checked;

    /**
     * 子集
     */
    private List<PermissionRespNode> children;

    /**
     * vue页面菜单权限专用
     */
    private String path;

    /**
     * vue页面菜单权限专用
     */
    private String icon;

    /**
     * VUE列表树专用
     */
    private boolean hasChildren;

    /**
     *vue专用  菜单视图路径
     */
    private String component;

    /**
     *创建日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
