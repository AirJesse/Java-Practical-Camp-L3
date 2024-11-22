package org.bank.domain.vo.resp;

import lombok.Data;

import java.io.Serializable;


@Data
public class MenuMetaVo implements Serializable {
    private String title;

    private String icon;

    private Boolean noCache;

    public MenuMetaVo(String title, String icon, boolean b) {
        this.title=title;
        this.icon=icon;
        this.noCache=b;
    }
}
