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
public class PermissionPageReqVO {

    /**
     * 第几页
     */
    private int pageNum=1;

    /**
     *分页数量
     */
    private int pageSize=10;

    /**
     *名称
     */
    private String name;

    /**
     *父id
     */
    private String pid;

    /**
     *是否返回按钮
     */
    private boolean notShowButton;
}
