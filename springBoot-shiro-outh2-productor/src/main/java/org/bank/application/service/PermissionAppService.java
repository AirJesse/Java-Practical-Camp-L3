package org.bank.application.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.bank.common.constant.Constant;
import org.bank.common.exception.BusinessException;
import org.bank.common.exception.code.BaseResponseCode;
import org.bank.common.util.PageUtils;
import org.bank.common.util.TokenSettings;
import org.bank.domain.entity.SysPermission;
import org.bank.domain.repository.SysPermissionRepository;
import org.bank.domain.service.PermissionService;
import org.bank.domain.service.RedisService;
import org.bank.domain.service.RolePermissionService;
import org.bank.domain.service.UserRoleService;
import org.bank.domain.vo.req.PermissionAddReqVO;
import org.bank.domain.vo.req.PermissionPageReqVO;
import org.bank.domain.vo.req.PermissionUpdateReqVO;
import org.bank.domain.vo.resp.PageVO;
import org.bank.domain.vo.resp.PermissionRespNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class PermissionAppService implements PermissionService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private SysPermissionRepository sysPermissionRepository;
    @Autowired
    private TokenSettings tokenSettings;

    /**
     * 根据用户查询拥有的权限
     * 先查出用户拥有的角色
     * 再去差用户拥有的权限
     * 也可以多表关联查询
     */
    @Override
    public List<SysPermission> getPermission(String userId) {
        List<String> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return null;
        }
        List<String> permissionIds = rolePermissionService.getPermissionIdsByRoles(roleIds);
        if (permissionIds.isEmpty()) {
            return null;
        }
        List<SysPermission> result = sysPermissionRepository.selectInfoByIds(permissionIds);
        return result;
    }

    /**
     * 新增菜单权限
     */
    @Override
    public SysPermission addPermission(PermissionAddReqVO vo) {
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(vo, sysPermission);
        verifyForm(sysPermission);
        sysPermission.setId(UUID.randomUUID().toString());
        sysPermission.setCreateTime(new Date());
        int count = sysPermissionRepository.insertSelective(sysPermission);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysPermission;
    }

    /**
     * 操作后的菜单类型是目录的时候 父级必须为目录
     * 操作后的菜单类型是菜单的时候，父类必须为目录类型
     * 操作后的菜单类型是按钮的时候 父类必须为菜单类型
     */
    private void verifyFormPid(SysPermission sysPermission) {
        SysPermission parent = sysPermissionRepository.selectByPrimaryKey(sysPermission.getPid());
        switch (sysPermission.getType()) {
            case 1:
                if (parent != null) {
                    if (parent.getType() != 1) {
                        throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                    }
                } else if (!sysPermission.getPid().equals("0")) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                }
                break;
            case 2:
                if (parent == null || parent.getType() != 1) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_MENU_ERROR);
                }
                if (StringUtils.isEmpty(sysPermission.getUrl())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }

                break;
            case 3:
                if (parent == null || parent.getType() != 2) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_BTN_ERROR);
                }
                if (StringUtils.isEmpty(sysPermission.getPerms())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_PERMS_NULL);
                }
                if (StringUtils.isEmpty(sysPermission.getUrl())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
                if (StringUtils.isEmpty(sysPermission.getMethod())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_METHOD_NULL);
                }
                break;
        }
    }

    /**
     * 编辑或者新增的时候检验
     */
    private void verifyForm(SysPermission sysPermission) {

        verifyFormPid(sysPermission);
        /**
         * id 不为空说明是编辑
         */
        if (!StringUtils.isEmpty(sysPermission.getId())) {
            List<SysPermission> list = sysPermissionRepository.selectChild(sysPermission.getId());
            if (!list.isEmpty()) {
                throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_UPDATE);
            }
        }

    }

    /**
     * 查询菜单权限详情
     */
    @Override
    public SysPermission detailInfo(String permissionId) {

        return sysPermissionRepository.selectByPrimaryKey(permissionId);
    }

    /**
     * 更新菜单权限
     *
     * @return void
     * @throws
     */
    @Override
    public void updatePermission(PermissionUpdateReqVO vo) {

        SysPermission sysPermission = sysPermissionRepository.selectByPrimaryKey(vo.getId());
        if (null == sysPermission) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        SysPermission update = new SysPermission();
        BeanUtils.copyProperties(vo, update);
        /**
         * 只有类型变更
         * 或者所属菜单变更
         */
        if (sysPermission.getType() != vo.getType() || !sysPermission.getPid().equals(vo.getPid())) {
            verifyForm(update);
        }
        update.setUpdateTime(new Date());
        int count = sysPermissionRepository.updateByPrimaryKeySelective(update);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        /**
         * 说明这个菜单权限 权鉴标识发生变化
         * 所有管理这个菜单权限用户将重新刷新token
         */
        if (StringUtils.isEmpty(vo.getPerms()) && sysPermission.getPerms() != null && !sysPermission.getPerms().equals(vo.getPerms())) {
            List<String> roleIds = rolePermissionService.getRoleIds(vo.getId());
            if (!roleIds.isEmpty()) {
                List<String> userIds = userRoleService.getUserIdsByRoleIds(roleIds);
                if (!userIds.isEmpty()) {
                    for (String userId : userIds) {
                        redisService.set(Constant.JWT_REFRESH_KEY + userId, userId, tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                        //清空权鉴缓存
                        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
                    }

                }
            }
        }

    }

    /**
     * 删除菜单权限
     * 判断是否 有角色关联
     * 判断是否有子集
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void deleted(String permissionId) {
        SysPermission sysPermission = sysPermissionRepository.selectByPrimaryKey(permissionId);
        if (null == sysPermission) {
            log.error("传入 的 id:{}不合法", permissionId);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        List<SysPermission> childs = sysPermissionRepository.selectChild(permissionId);
        if (!childs.isEmpty()) {
            throw new BusinessException(BaseResponseCode.ROLE_PERMISSION_RELATION);
        }
        sysPermission.setDeleted(0);
        sysPermission.setUpdateTime(new Date());
        int count = sysPermissionRepository.updateByPrimaryKeySelective(sysPermission);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        /**
         * 删除和角色关联
         */
        rolePermissionService.removeByPermissionId(permissionId);
        List<String> roleIds = rolePermissionService.getRoleIds(permissionId);
        if (!roleIds.isEmpty()) {
            List<String> userIds = userRoleService.getUserIdsByRoleIds(roleIds);
            if (!userIds.isEmpty()) {
                for (String userId : userIds) {
                    redisService.set(Constant.JWT_REFRESH_KEY + userId, userId, tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                    //清空权鉴缓存
                    redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
                }

            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void deleted(List<String> permissionIds) {
        if (permissionIds != null && permissionIds.size() > 0) {
            for (String id : permissionIds) {
                deleted(id);
            }
        }
    }

    /**
     * 分页获取所有菜单权限
     */
    @Override
    public PageVO<SysPermission> pageInfo(PermissionPageReqVO vo) {

        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<SysPermission> list = selectAll();
        return PageUtils.getPageVO(list);
    }

    @Override
    public PageVO<PermissionRespNode> pageInfoForVue(PermissionPageReqVO vo) {
        Page<PermissionRespNode> page = PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        if (StringUtils.isEmpty(vo.getPid()) && StringUtils.isEmpty(vo.getName())) {
            vo.setPid("0");
        }
        List<SysPermission> list = sysPermissionRepository.selectAll(vo);
        List<SysPermission> allList = sysPermissionRepository.selectAll(null);
        //组装LIST
        List<PermissionRespNode> permissionRespNodes = null;
        if (list != null && list.size() > 0) {
            permissionRespNodes = new ArrayList<>();
            for (SysPermission sysPermission : list) {
                List<PermissionRespNode> childPermList = new ArrayList<>();
                PermissionRespNode respNode = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, respNode);
                respNode.setTitle(sysPermission.getName());
                respNode.setLabel(sysPermission.getName());
                respNode.setSpread(true);
                List<PermissionRespNode> childrenList = null;
                if (vo.isNotShowButton()) {
                    childrenList = getChildExcBtn(sysPermission.getId(), allList);
                } else {
                    childrenList = getChildAll(sysPermission.getId(), allList);
                }
                //此处为了兼容vue页面选择
                respNode.setChildren(null);
                if (childrenList != null && childrenList.size() > 0) {
                    respNode.setHasChildren(true);
                } else {
                    respNode.setHasChildren(false);
                }
                permissionRespNodes.add(respNode);
            }
        }
        PageVO<PermissionRespNode> result = new PageVO<>();
        result.setTotalRows(permissionRespNodes == null ? 0l : permissionRespNodes.size());
        result.setTotalPages(1);
        result.setPageNum(page.getPageNum());
        result.setPageSize(page.getPageSize());
        result.setCurPageSize(page.getPageSize());
        result.setList(permissionRespNodes);
        return result;
    }

    /**
     * 获取所有菜单权限
     */
    @Override
    public List<SysPermission> selectAll() {
        List<SysPermission> result = sysPermissionRepository.selectAll(null);
        if (!result.isEmpty()) {
            for (SysPermission sysPermission : result) {
                SysPermission parent = sysPermissionRepository.selectByPrimaryKey(sysPermission.getPid());
                if (parent != null) {
                    sysPermission.setPidName(parent.getName());
                }
            }
        }
        return result;
    }

    /**
     * 获取权限标识
     */
    @Override
    public Set<String> getPermissionsByUserId(String userId) {

        List<SysPermission> list = getPermission(userId);
        Set<String> permissions = new HashSet<>();
        if (null == list || list.isEmpty()) {
            return null;
        }
        for (SysPermission sysPermission : list) {
            if (!StringUtils.isEmpty(sysPermission.getPerms())) {
                permissions.add(sysPermission.getPerms());
            }

        }
        return permissions;
    }

    /**
     * 获取权限的所有子权限
     *
     * @param permId
     * @return
     */
    @Override
    public List<String> getChildrenIds(String permId) {
        List<String> childrenList = new ArrayList<>();
        List<SysPermission> list;
        List<SysPermission> all = list = sysPermissionRepository.selectAll(null);
        if (StringUtils.isEmpty(permId)) {
            PermissionPageReqVO vo = new PermissionPageReqVO();
            vo.setPid("0");
            list = sysPermissionRepository.selectAll(vo);
        } else {
            SysPermission sysPermission = sysPermissionRepository.selectByPrimaryKey(permId);
            if (sysPermission == null) {
                throw new BusinessException(BaseResponseCode.PERMISSION_ERROR);
            }
            childrenList.add(permId);
            list = new ArrayList<>();
            list.add(sysPermission);
        }
        if (list == null && list.size() == 0) {
            return childrenList;
        }
        for (SysPermission perm : list) {
            if (perm == null) continue;
            getChildIds(perm.getId(), all, childrenList);
        }
        return childrenList;
    }


    /**
     * 递归遍历所有
     */
    private void getChildIds(String id, List<SysPermission> all, List<String> childrenList) {
        List<PermissionRespNode> list = new ArrayList<>();
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals(id)) {
                childrenList.add(sysPermission.getId());
                getChildIds(sysPermission.getId(), all, childrenList);
            }
        }
    }

    /**
     * 以树型的形式把用户拥有的菜单权限返回给客户端
     */
    @Override
    public List<PermissionRespNode> permissionTreeList(String userId) {
        List<SysPermission> list = getPermission(userId);
        return getTree(list, true);
    }

    /**
     * 递归获取菜单树
     */
    private List<PermissionRespNode> getTree(List<SysPermission> all, boolean type) {

        List<PermissionRespNode> list = new ArrayList<>();
        if (all == null || all.isEmpty()) {
            return list;
        }
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals("0")) {
                PermissionRespNode permissionRespNode = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, permissionRespNode);
                permissionRespNode.setTitle(sysPermission.getName());
                permissionRespNode.setLabel(sysPermission.getName());
                if (type) {
                    permissionRespNode.setChildren(getChildExcBtn(sysPermission.getId(), all));
                } else {
                    permissionRespNode.setChildren(getChildAll(sysPermission.getId(), all));
                }
                list.add(permissionRespNode);
            }
        }
        return list;
    }

    /**
     * 递归遍历所有
     */
    private List<PermissionRespNode> getChildAll(String id, List<SysPermission> all) {

        List<PermissionRespNode> list = new ArrayList<>();
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals(id)) {
                PermissionRespNode permissionRespNode = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, permissionRespNode);
                permissionRespNode.setTitle(sysPermission.getName());
                permissionRespNode.setLabel(sysPermission.getName());

                permissionRespNode.setChildren(getChildAll(sysPermission.getId(), all));
                list.add(permissionRespNode);
            }
        }
        return list;
    }

    /**
     * 只递归获取目录和菜单
     */
    private List<PermissionRespNode> getChildExcBtn(String id, List<SysPermission> all) {

        List<PermissionRespNode> list = new ArrayList<>();
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals(id) && sysPermission.getType() != 3) {
                PermissionRespNode permissionRespNode = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, permissionRespNode);
                permissionRespNode.setTitle(sysPermission.getName());
                permissionRespNode.setLabel(sysPermission.getName());
                permissionRespNode.setChildren(getChildExcBtn(sysPermission.getId(), all));
                list.add(permissionRespNode);
            }
        }
        return list;
    }

    /**
     * 获取所有菜单权限按钮
     */
    @Override
    public List<PermissionRespNode> selectAllByTree() {

        List<SysPermission> list = selectAll();
        return getTree(list, false);
    }

    @Override
    public List<PermissionRespNode> getALLmenuTreeList(String filterMenuId) {
        PermissionPageReqVO vo = new PermissionPageReqVO();
        vo.setPid("0");
        List<SysPermission> list = sysPermissionRepository.selectAll(vo);
        List<SysPermission> allList = sysPermissionRepository.selectAll(null);
        //组装LIST
        List<PermissionRespNode> sysmenus = null;
        if (list != null && list.size() > 0) {
            sysmenus = new ArrayList<>();
            for (SysPermission menu : list) {
                if (!StringUtils.isEmpty(filterMenuId) && filterMenuId.equals(menu.getId())) {
                    continue;
                }
                List<SysPermission> childDeptList = new ArrayList<>();
                PermissionRespNode menuTree = new PermissionRespNode();
                BeanUtils.copyProperties(menu, menuTree);
                menuTree.setTitle(menu.getName());
                menuTree.setLabel(menu.getName());
                menuTree.setSpread(true);
                List<PermissionRespNode> childrenList = getChildAll(menu.getId(), allList);
                //此处为了兼容vue页面选择
                menuTree.setChildren(childrenList);
                if (childrenList != null && childrenList.size() > 0) {
                    menuTree.setHasChildren(true);
                } else {
                    menuTree.setHasChildren(false);
                }
                sysmenus.add(menuTree);
            }
        }
        List<PermissionRespNode> result = new ArrayList<>();
        PermissionRespNode respNode = new PermissionRespNode();
        respNode.setId("0");
        respNode.setTitle("默认顶级菜单");
        respNode.setLabel("默认顶级菜单");
        respNode.setName("默认顶级菜单");
        respNode.setSpread(true);
        if (sysmenus != null && sysmenus.size() > 0) {
            respNode.setHasChildren(true);
            respNode.setChildren(sysmenus);
        } else {
            respNode.setHasChildren(false);
        }
        result.add(respNode);
        return result;
    }

    /**
     * 获取所有的目录菜单树排除按钮
     * 因为不管是新增或者修改
     * 选择所属菜单目录的时候
     * 都不可能选择到按钮
     * 而且编辑的时候 所属目录不能
     * 选择自己和它的子类
     */
    @Override
    public List<PermissionRespNode> selectAllMenuByTree(String permissionId) {

        List<SysPermission> list = selectAll();
        if (!list.isEmpty() && !StringUtils.isEmpty(permissionId)) {
            for (SysPermission sysPermission : list) {
                if (sysPermission.getId().equals(permissionId)) {
                    list.remove(sysPermission);
                    break;
                }
            }
        }
        List<PermissionRespNode> result = new ArrayList<>();
        //新增顶级目录是为了方便添加一级目录
        PermissionRespNode respNode = new PermissionRespNode();
        respNode.setId("0");
        respNode.setTitle("默认顶级菜单");
        respNode.setSpread(true);
        respNode.setChildren(getTree(list, true));
        result.add(respNode);
        return result;
    }
}
