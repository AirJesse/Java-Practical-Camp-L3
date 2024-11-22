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
public class DeptAddReqVO {

    /**
     * 机构名称
     */
    @NotBlank(message = "机构名称不能为空")
    private String name;

    /**
     * 父级id
     */
    @NotBlank(message = "父级id不能为空")
    private String pid;

    /**
     *部门经理id
     */
    private String deptManagerId;

    /**
     *部门经理名称
     */
    private String managerName;

    /**
     *部门经理电话
     */
    private String phone;

    /**
     *机构状态(1:正常；0:弃用)
     */
    private Integer status;

}
