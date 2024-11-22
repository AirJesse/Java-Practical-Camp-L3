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
public class HomeRespVO {
    /**
     * 用户信息
     */
    private UserInfoRespVO userInfo;
    /**
     * 目录菜单
     */
    private List<PermissionRespNode> menus;

}