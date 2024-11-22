package org.bank.application.service;


import org.bank.common.exception.BusinessException;
import org.bank.common.exception.code.BaseResponseCode;
import org.bank.domain.entity.SysUserRole;
import org.bank.domain.repository.SysUserRoleRepository;
import org.bank.domain.service.UserRoleService;
import org.bank.domain.vo.req.UserRoleOperationReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class UserRoleAppService implements UserRoleService {
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Override
    public int removeByRoleId(String roleId) {
        return sysUserRoleRepository.removeByRoleId(roleId);
    }

    @Override
    public List<String> getRoleIdsByUserId(String userId) {
        return sysUserRoleRepository.getRoleIdsByUserId(userId);
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUserRoleInfo(UserRoleOperationReqVO vo) {
        if(vo.getRoleIds()==null||vo.getRoleIds().isEmpty()){
            return;
        }
        Date createTime=new Date();
        List<SysUserRole> list=new ArrayList<>();
        for (String roleId:vo.getRoleIds()){
            SysUserRole sysUserRole=new SysUserRole();
            sysUserRole.setId(UUID.randomUUID().toString());
            sysUserRole.setCreateTime(createTime);
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        }
        sysUserRoleRepository.removeByUserId(vo.getUserId());
        int count=sysUserRoleRepository.batchUserRole(list);
        if (count==0){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public int removeByUserId(String userId) {

        return sysUserRoleRepository.removeByUserId(userId);
    }

    @Override
    public List<String> getUserIdsByRoleIds(List<String> roleIds) {

        return sysUserRoleRepository.getUserIdsByRoleIds(roleIds);
    }
}
