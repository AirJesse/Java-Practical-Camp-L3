package org.bank.domain.repository;


import org.apache.ibatis.annotations.Param;
import org.bank.domain.entity.SysUser;
import org.bank.domain.vo.req.UserPageReqVO;

import java.util.List;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.repository
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
public interface SysUserRepository {
    int deleteByPrimaryKey(String id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser getUserInfoByName(String username);

    List<SysUser> selectAll(UserPageReqVO vo);

    List<SysUser> selectUserInfoByDeptIds (List<String> deptIds);

    List<SysUser> getUserListByDeptId(String deptId);

    int deletedUsers(@Param("sysUser") SysUser sysUser,@Param("list") List<String> list);
}