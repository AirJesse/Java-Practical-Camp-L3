package org.bank.domain.vo.resp;

import lombok.Data;

import java.util.List;

@Data
public class HomeVueRespVO {
    /**
     * 用户信息
     */
    private UserInfoRespVO userInfo;
    /**
     * 目录菜单
     */
    private List<MenuVo> menus;
}
