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
public class OuthClientDetailsUpdateReqVO {
    /**
     *机构id
     */
    @NotBlank(message = "资源id不能为空")
    private String id;

    /**
     *客户端名称名称
     */
    @NotBlank(message = "客户端名称名称不能为空")
    private String clientName;

    /**
     *客户端密钥
     */
    private String clientNecret;
}
