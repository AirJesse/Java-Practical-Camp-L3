package org.bank.domain.vo.req;

import lombok.Data;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.req
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class DeptPageReqVO {
    /**
     * 第几页
     */
    private int pageNum=1;

    /**
     *分页数量
     */
    private int pageSize=10;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 状态
     */
    private String status;

    /**
     * 父ID
     */
    private String pid;
}
