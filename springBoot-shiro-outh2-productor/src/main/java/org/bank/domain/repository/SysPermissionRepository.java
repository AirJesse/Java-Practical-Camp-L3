package org.bank.domain.repository;


import org.bank.domain.entity.SysPermission;
import org.bank.domain.vo.req.PermissionPageReqVO;

import java.util.List;

public interface SysPermissionRepository {
    int deleteByPrimaryKey(String id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> selectInfoByIds (List<String> ids);

    List<SysPermission> selectAll(PermissionPageReqVO vo);

    List<SysPermission> selectChild(String pid);
}