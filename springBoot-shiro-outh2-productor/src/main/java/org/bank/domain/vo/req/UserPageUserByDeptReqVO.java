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
public class UserPageUserByDeptReqVO {

    /**
     * 第几页
     */
    private int pageNum=1;

    /**
     * 分页数量
     */
    private int pageSize=10;

    /**
     * 组织id
     */
    @NotBlank(message ="组织id不能为空")
    private String deptId;
}
