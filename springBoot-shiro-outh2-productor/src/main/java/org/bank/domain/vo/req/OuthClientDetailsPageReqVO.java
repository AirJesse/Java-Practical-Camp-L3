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
public class OuthClientDetailsPageReqVO {
    /**
     * 第几页
     */
    private int pageNum=1;

    /**
     *分页数量
     */
    private int pageSize=10;

    /**
     *客户端名称
     */
    private String clientName;

    /**
     *客户端Id
     */
    private String clientId;

    /**
     *客户端密码
     */
    private String clientSecret;
}
