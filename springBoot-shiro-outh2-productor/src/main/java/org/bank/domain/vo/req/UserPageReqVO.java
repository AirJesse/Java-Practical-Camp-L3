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
public class UserPageReqVO {

    /**
     * 第几页
     */
    private int pageNum=1;

    /**
     * 分页数量
     */
    private int pageSize=10;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 账号
     */
    private String username;


    /**
     * 账户状态(1.正常 2.锁定)
     */
    private Integer status;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 部门Id
     */
    private String deptId;

}
