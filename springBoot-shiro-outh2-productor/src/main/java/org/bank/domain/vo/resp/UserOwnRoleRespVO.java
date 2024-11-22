package org.bank.domain.vo.resp;


import lombok.Data;
import org.bank.domain.entity.SysRole;

import java.util.List;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.vo.resp
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class UserOwnRoleRespVO {

    /**
     * 所有角色集合
     */
    private List<SysRole> allRole;

    /**
     * 用户所拥有角色集合
     */
    private List<String> ownRoles;
}
