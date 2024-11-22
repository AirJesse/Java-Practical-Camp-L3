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
public class RolePageReqVO {
    /**
     * 第几页
     */
    private int pageNum=1;

    /**
     * 分页数量
     */
    private int pageSize=10;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 状态(1:正常0:弃用)
     */
    private Integer status;
}
