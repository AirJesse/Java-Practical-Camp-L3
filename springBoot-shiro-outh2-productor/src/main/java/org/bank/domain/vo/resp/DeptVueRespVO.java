package org.bank.domain.vo.resp;

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
public class DeptVueRespVO {
    /**
     * 组织id
     */
    private String id;

    /**
     * 组织编码
     */
    private String deptNo;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织父级id
     */
    private String pid;

    /**
     * 组织状态
     */
    private Integer status;

    /**
     * 组织状态
     */
    private Date createTime;

    /**
     * 组织关系id
     */
    private String relationCode;

    /**
     * 是否展开 默认不展开(false)
     */
    private boolean spread;

    /**
     * 子集
     */
    private List<?> children;
}
