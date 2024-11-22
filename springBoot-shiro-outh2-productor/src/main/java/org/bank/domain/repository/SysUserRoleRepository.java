package org.bank.domain.repository;




import org.bank.domain.entity.SysUserRole;

import java.util.List;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.repository
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
public interface SysUserRoleRepository {
    int deleteByPrimaryKey(String id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    int removeByRoleId(String roleId);

    List<String> getRoleIdsByUserId(String userId);

    int batchUserRole(List<SysUserRole> list);

    int removeByUserId(String userId);

    List<String> getInfoByUserIdByRoleId( String roleId);

    List<String> getUserIdsByRoleIds(List<String> roleIds);

}