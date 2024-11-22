package org.bank.domain.vo.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.req
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class OuthClientDetailsAddReqVO {

    /**
     * 客户端名称
     */
    @NotBlank(message = "机构名称不能为空")
    private String clientName;

    /**
     * 客户端Id
     */
    private String clientId;


    /**
     *客户端密钥
     */
    private String clientSecret;


}
