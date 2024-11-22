package org.bank.application.service;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.bank.common.constant.Constant;
import org.bank.common.exception.BusinessException;
import org.bank.common.exception.code.BaseResponseCode;
import org.bank.common.util.PageUtils;
import org.bank.common.util.TokenSettings;
import org.bank.domain.entity.SysRole;
import org.bank.domain.repository.SysRoleRepository;
import org.bank.domain.repository.SysUserRoleRepository;
import org.bank.domain.service.*;
import org.bank.domain.vo.req.RoleAddReqVO;
import org.bank.domain.vo.req.RolePageReqVO;
import org.bank.domain.vo.req.RolePermissionOperationReqVO;
import org.bank.domain.vo.req.RoleUpdateReqVO;
import org.bank.domain.vo.resp.PageVO;
import org.bank.domain.vo.resp.PermissionRespNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private SysRoleRepository sysRoleRepository;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TokenSettings tokenSettings;
    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;
    @Autowired
    private PermissionService permissionService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysRole addRole(RoleAddReqVO vo) {

        SysRole sysRole=new SysRole();
        BeanUtils.copyProperties(vo,sysRole);
        sysRole.setId(UUID.randomUUID().toString());
        sysRole.setCreateTime(new Date());
        int count= sysRoleRepository.insertSelective(sysRole);
        if(count!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        if(null!=vo.getPermissions()&&!vo.getPermissions().isEmpty()){
            RolePermissionOperationReqVO reqVO=new RolePermissionOperationReqVO();
            reqVO.setRoleId(sysRole.getId());
            reqVO.setPermissionIds(vo.getPermissions());
            rolePermissionService.addRolePermission(reqVO);
        }

        return sysRole;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(RoleUpdateReqVO vo, String accessToken) {
        SysRole sysRole=sysRoleRepository.selectByPrimaryKey(vo.getId());
        if (null==sysRole){
            log.error("传入 的 id:{}不合法",vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        SysRole update=new SysRole();
        BeanUtils.copyProperties(vo,update);
//        BeanUtils.copyProperties(vo,sysRole);
        update.setUpdateTime(new Date());
        int count=sysRoleRepository.updateByPrimaryKeySelective(update);
        if(count!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        rolePermissionService.removeByRoleId(sysRole.getId());
        if(null!=vo.getPermissions()&&!vo.getPermissions().isEmpty()){
            RolePermissionOperationReqVO reqVO=new RolePermissionOperationReqVO();
            reqVO.setRoleId(sysRole.getId());
            reqVO.setPermissionIds(vo.getPermissions());
            rolePermissionService.addRolePermission(reqVO);

            List<String> userIds=sysUserRoleRepository.getInfoByUserIdByRoleId(vo.getId());

            if(!userIds.isEmpty()){
                for (String userId:userIds){
                    redisService.set(Constant.JWT_REFRESH_KEY +userId,userId,tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                    //清空权鉴缓存
                    redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
                }

            }

        }

    }

    @Override
    public SysRole detailInfo(String id) {
        SysRole sysRole = sysRoleRepository.selectByPrimaryKey(id);
        if(sysRole==null){
            log.error("传入 的 id:{}不合法",id);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        List<PermissionRespNode> permissionRespNodes = permissionService.selectAllByTree();
        Set<String> checkList=new HashSet<>(rolePermissionService.getPermissionIdsByRoleId(sysRole.getId()));
        setheckced(permissionRespNodes,checkList);
        sysRole.setPermissionRespNodes(permissionRespNodes);
        //获取其权限
        List<String> permIds=rolePermissionService.getPermissionIdsByRoleId(id);
        sysRole.setPermIds(permIds);
        return sysRole;
    }


    private void setheckced(List<PermissionRespNode> list, Set<String> checkList){

        for(PermissionRespNode node:list){

            if(checkList.contains(node.getId())&&(node.getChildren()==null||node.getChildren().isEmpty())){
                node.setChecked(true);
            }
            setheckced((List<PermissionRespNode>) node.getChildren(),checkList);

        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletedRole(String id) {
        SysRole sysRole=new SysRole();
        sysRole.setId(id);
        sysRole.setUpdateTime(new Date());
        sysRole.setDeleted(0);
        int count=sysRoleRepository.updateByPrimaryKeySelective(sysRole);
        if (count!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        List<String> userIds=sysUserRoleRepository.getInfoByUserIdByRoleId(id);
        rolePermissionService.removeByRoleId(id);
        userRoleService.removeByRoleId(id);

        if(!userIds.isEmpty()){
            for (String userId:userIds){
                redisService.set(Constant.JWT_REFRESH_KEY +userId,userId,tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                //清空权鉴缓存
                redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
            }

        }
    }

    @Override
    public PageVO<SysRole> pageInfo(RolePageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<SysRole> sysRoles =sysRoleRepository.selectAll(vo);
        //获取其权限集合
        if(sysRoles!=null&&sysRoles.size()>0){
            for(SysRole role:sysRoles){
                String roleId=role.getId();
                //获取其权限
                List<String> permIds=rolePermissionService.getPermissionIdsByRoleId(roleId);
                role.setPermIds(permIds);
            }
        }
        return PageUtils.getPageVO(sysRoles);
    }

    @Override
    public List<SysRole> getRoleInfoByUserId(String userId) {

        List<String> roleIds=userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()){
            return null;
        }
        return sysRoleRepository.getRoleInfoByIds(roleIds);
    }

    @Override
    public List<String> getRoleNames(String userId) {

        List<SysRole> sysRoles=getRoleInfoByUserId(userId);
        if (null==sysRoles||sysRoles.isEmpty()){
            return null;
        }
        List<String> list=new ArrayList<>();
        for (SysRole sysRole:sysRoles){
            list.add(sysRole.getName());
        }
        return list;
    }

    @Override
    public List<SysRole> selectAllRoles() {

        return sysRoleRepository.selectAll(new RolePageReqVO());
    }

}
