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
public class DeptUpdateReqVO {
    /**
     *机构id
     */
    @NotBlank(message = "机构id不能为空")
    private String id;

    /**
     *机构名称
     */
    private String name;

    /**
     *父级id
     */
    private String pid;

    /**
     *机构状态(1:正常；0:弃用)
     */
    private Integer status;

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
}
