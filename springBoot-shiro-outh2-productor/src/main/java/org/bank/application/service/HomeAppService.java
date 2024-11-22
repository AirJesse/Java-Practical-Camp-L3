package org.bank.application.service;

import com.alibaba.druid.util.StringUtils;
import org.bank.domain.entity.SysDept;
import org.bank.domain.entity.SysUser;
import org.bank.domain.service.DeptService;
import org.bank.domain.service.HomeService;
import org.bank.domain.service.PermissionService;
import org.bank.domain.service.UserService;
import org.bank.domain.vo.resp.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service
public class HomeAppService implements HomeService {
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public HomeRespVO getHomeInfo(String userId) {

        SysUser sysUser=userService.detailInfo(userId);
        UserInfoRespVO vo=new UserInfoRespVO();
        if(sysUser!=null){
            BeanUtils.copyProperties(sysUser, vo);
            SysDept sysDept = deptService.detailInfo(sysUser.getDeptId());
            if(sysDept!=null){
                vo.setDeptId(sysDept.getId());
                vo.setDeptName(sysDept.getName());
            }

        }
        List<PermissionRespNode> menus = permissionService.permissionTreeList(userId);
        HomeRespVO respVO=new HomeRespVO();
        respVO.setMenus(menus);
        respVO.setUserInfo(vo);

        return respVO;
    }

    @Override
    public HomeVueRespVO homeForVue(String userId) {
        SysUser sysUser=userService.detailInfo(userId);
        UserInfoRespVO vo=new UserInfoRespVO();
        if(sysUser!=null){
            BeanUtils.copyProperties(sysUser, vo);
            SysDept sysDept = deptService.detailInfo(sysUser.getDeptId());
            if(sysDept!=null){
                vo.setDeptId(sysDept.getId());
                vo.setDeptName(sysDept.getName());
            }

        }

        List<PermissionRespNode> menus = permissionService.permissionTreeList(userId);
        List<MenuVo> list=buildMenus(menus);
        HomeVueRespVO respVO=new HomeVueRespVO();
        respVO.setMenus(list);
        respVO.setUserInfo(vo);

        return respVO;
    }

    public List<MenuVo> buildMenus(List<PermissionRespNode> menus) {
        List<MenuVo> list = new LinkedList<>();
        menus.forEach(menuDTO -> {
                    if (menuDTO!=null){
                        List<PermissionRespNode> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(menuDTO.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath("0".equals(menuDTO.getPid())  ? "/" + menuDTO.getPath() :menuDTO.getPath());
                        menuVo.setHidden(false);
                        // 如果不是外链
                        if("0".equals(menuDTO.getPid())){
                            menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent())?"Layout":menuDTO.getComponent());
                            // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
                        }else if(menuDTO.getType() == 1){
                            menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent())?"ParentView":menuDTO.getComponent());
                        }else if(!StringUtils.isEmpty(menuDTO.getComponent())){
                            menuVo.setComponent(menuDTO.getComponent());
                        }

                        menuVo.setMeta(new MenuMetaVo(menuDTO.getTitle(),menuDTO.getIcon(),true));
                        if(menuDtoList!=null&&menuDtoList.size()>0){
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if("0".equals(menuDTO.getPid())){
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            menuVo1.setPath("index");
                            menuVo1.setName(menuVo.getName());
                            menuVo1.setComponent(menuVo.getComponent());
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }
}
