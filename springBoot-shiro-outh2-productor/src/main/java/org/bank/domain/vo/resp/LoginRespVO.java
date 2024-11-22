package org.bank.domain.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.resp
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class LoginRespVO {

    /**
     * token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户id
     */
    private String id;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户所拥有的菜单权限(前后端分离返回给前端控制菜单和按钮的显示和隐藏)
     */
    private List<PermissionRespNode> list;
}
