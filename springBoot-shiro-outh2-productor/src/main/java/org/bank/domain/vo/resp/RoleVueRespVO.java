package org.bank.domain.vo.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class RoleVueRespVO {
    private String id;

    private String name;

    private String description;

    private Integer status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Date updateTime;

    private Integer deleted;

    private List<PermissionRespNode> permissionRespNodes;

}
