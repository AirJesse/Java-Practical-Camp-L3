package org.bank.application.service;


import org.bank.common.exception.BusinessException;
import org.bank.common.exception.code.BaseResponseCode;
import org.bank.domain.entity.SysRolePermission;
import org.bank.domain.repository.SysRolePermissionRepository;
import org.bank.domain.service.RolePermissionService;
import org.bank.domain.vo.req.RolePermissionOperationReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class RolePermissionAppService implements RolePermissionService {
    @Autowired
    private SysRolePermissionRepository sysRolePermissionRepository;

    @Override
    public int removeByRoleId(String roleId) {
        return sysRolePermissionRepository.removeByRoleId(roleId);
    }

    @Override
    public List<String> getPermissionIdsByRoles(List<String> roleIds) {

        return sysRolePermissionRepository.getPermissionIdsByRoles(roleIds);
    }

    @Override
    public void addRolePermission(RolePermissionOperationReqVO vo) {

        Date createTime=new Date();
        List<SysRolePermission> list=new ArrayList<>();
        for (String permissionId:vo.getPermissionIds()){
            SysRolePermission sysRolePermission=new SysRolePermission();
            sysRolePermission.setId(UUID.randomUUID().toString());
            sysRolePermission.setCreateTime(createTime);
            sysRolePermission.setPermissionId(permissionId);
            sysRolePermission.setRoleId(vo.getRoleId());
            list.add(sysRolePermission);
        }
        sysRolePermissionRepository.removeByRoleId(vo.getRoleId());
        if(list==null||list.size()==0){
            throw  new BusinessException(BaseResponseCode.PERMISSIONS_ISEMPTY_ERROR);
        }
        int count=sysRolePermissionRepository.batchRolePermission(list);
        if (count==0){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }
    @Override
    public int removeByPermissionId(String permissionId) {

        return sysRolePermissionRepository.removeByPermissionId(permissionId);
    }

    @Override
    public List<String> getRoleIds(String permissionId) {

        return sysRolePermissionRepository.getRoleIds(permissionId);
    }

    @Override
    public List<String> getPermissionIdsByRoleId(String roleId) {

        return sysRolePermissionRepository.getPermissionIdsByRoleId(roleId);
    }
}
