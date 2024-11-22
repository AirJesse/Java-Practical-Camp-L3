package org.bank.domain.vo.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MenuVo implements Serializable {
    private String name;

    private String path;

    private Boolean hidden;

    private String redirect;

    private String component;

    private Boolean alwaysShow;

    private MenuMetaVo meta;

    private List<MenuVo> children;
}
