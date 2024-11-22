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
public class SysLogPageReqVO {

    /**
     * 第几页
     */
    private int pageNum=1;

    /**
     * 分页数量
     */
    private int pageSize=10;

    /**
     * 用户操作动作
     */
    private String operation;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 账号
     */
    private String username;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
}
