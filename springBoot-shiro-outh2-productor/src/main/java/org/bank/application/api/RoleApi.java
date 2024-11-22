package org.bank.application.api;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bank.common.constant.Constant;
import org.bank.common.util.DataResult;
import org.bank.domain.aop.annotation.LogAnnotation;
import org.bank.domain.entity.SysRole;
import org.bank.domain.service.RoleService;
import org.bank.domain.vo.req.RoleAddReqVO;
import org.bank.domain.vo.req.RolePageReqVO;
import org.bank.domain.vo.req.RoleUpdateReqVO;
import org.bank.domain.vo.resp.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.controller
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@RequestMapping("/sys")
@RestController
public class RoleApi {
    @Autowired
    private RoleService roleService;

    @PostMapping("/role")
    @LogAnnotation(title = "角色管理",action = "新增角色")
    @RequiresPermissions("sys:role:add")
    public DataResult<SysRole> addRole(@RequestBody @Valid RoleAddReqVO vo){
        DataResult<SysRole> result= DataResult.success();
        result.setData(roleService.addRole(vo));
        return result;
    }

    @DeleteMapping("/role/{id}")
    @LogAnnotation(title = "角色管理",action = "删除角色")
    @RequiresPermissions("sys:role:deleted")
    public DataResult deleted(@PathVariable("id") String id){
        roleService.deletedRole(id);
        return DataResult.success();
    }

    @PutMapping("/role")
    @LogAnnotation(title = "角色管理",action = "更新角色信息")
    @RequiresPermissions("sys:role:update")
    public DataResult updateDept(@RequestBody @Valid RoleUpdateReqVO vo, HttpServletRequest request){
        roleService.updateRole(vo,request.getHeader(Constant.ACCESS_TOKEN));
        return DataResult.success();
    }

    @GetMapping("/role/{id}")
    @LogAnnotation(title = "角色管理",action = "查询角色详情")
    @RequiresPermissions("sys:role:detail")
    public DataResult<SysRole> detailInfo(@PathVariable("id") String id){
        DataResult<SysRole> result=DataResult.success();
        result.setData(roleService.detailInfo(id));
        return result;
    }

    @PostMapping("/roles")
    @LogAnnotation(title = "角色管理",action = "分页获取角色信息")
    @RequiresPermissions("sys:role:list")
    public DataResult<PageVO<SysRole>> pageInfo(@RequestBody RolePageReqVO vo){
        DataResult<PageVO<SysRole>> result=DataResult.success();
        result.setData(roleService.pageInfo(vo));
        return result;
    }

    @GetMapping("/role/getAll")
    @LogAnnotation(title = "角色管理",action = "获取所有角色信息")
    @RequiresPermissions("sys:role:list")
    public DataResult<List<SysRole>> getAll(){
        DataResult<List<SysRole>> result=DataResult.success();
        result.setData(roleService.selectAllRoles());
        return result;
    }

}
