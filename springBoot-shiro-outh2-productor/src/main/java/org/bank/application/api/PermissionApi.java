package org.bank.application.api;


import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bank.common.util.DataResult;
import org.bank.domain.aop.annotation.LogAnnotation;
import org.bank.domain.entity.SysPermission;
import org.bank.domain.service.PermissionService;
import org.bank.domain.vo.req.PermissionAddReqVO;
import org.bank.domain.vo.req.PermissionPageReqVO;
import org.bank.domain.vo.req.PermissionUpdateReqVO;
import org.bank.domain.vo.resp.PageVO;
import org.bank.domain.vo.resp.PermissionRespNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class PermissionApi {
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/permission")
    @LogAnnotation(title = "菜单权限管理", action = "新增菜单权限")
    @RequiresPermissions("sys:permission:add")
    public DataResult<SysPermission> addPermission(@RequestBody @Valid PermissionAddReqVO vo) {
        DataResult<SysPermission> result = DataResult.success();
        result.setData(permissionService.addPermission(vo));
        return result;
    }

    @DeleteMapping("/permission/{id}")
    @LogAnnotation(title = "菜单权限管理", action = "删除菜单权限")
    @RequiresPermissions("sys:permission:deleted")
    public DataResult deleted(@PathVariable("id") String id) {
        DataResult result = DataResult.success();
        permissionService.deleted(id);
        return result;
    }

    @PostMapping("/permission/delete")
    @LogAnnotation(title = "机构管理", action = "删除组织")
    @RequiresPermissions("sys:dept:deleted")
    public DataResult batchDeleted(@RequestBody List<String> ids) {
        permissionService.deleted(ids);
        return DataResult.success();
    }

    @PutMapping("/permission")
    @LogAnnotation(title = "菜单权限管理", action = "更新菜单权限")
    @RequiresPermissions("sys:permission:update")
    public DataResult updatePermission(@RequestBody @Valid PermissionUpdateReqVO vo) {
        DataResult result = DataResult.success();
        permissionService.updatePermission(vo);
        return result;
    }

    @GetMapping("/permission/{id}")
    @LogAnnotation(title = "菜单权限管理", action = "查询菜单权限")
    @RequiresPermissions("sys:permission:detail")
    public DataResult<SysPermission> detailInfo(@PathVariable("id") String id) {
        DataResult<SysPermission> result = DataResult.success();
        result.setData(permissionService.detailInfo(id));
        return result;
    }

    @PostMapping("/permissionsForVue")
    @LogAnnotation(title = "菜单权限管理", action = "vue页面查询菜单权限")
    @RequiresPermissions("sys:permission:list")
    public DataResult<PageVO<PermissionRespNode>> pageInfoForVue(@RequestBody PermissionPageReqVO vo) {
        DataResult<PageVO<PermissionRespNode>> result = DataResult.success();
        result.setData(permissionService.pageInfoForVue(vo));
        return result;

    }

    @PostMapping("/permissions")
    @LogAnnotation(title = "菜单权限管理", action = "分页查询菜单权限")
    @RequiresPermissions("sys:permission:list")
    public DataResult<PageVO<SysPermission>> pageInfo(@RequestBody PermissionPageReqVO vo) {
        DataResult<PageVO<SysPermission>> result = DataResult.success();
        result.setData(permissionService.pageInfo(vo));
        return result;

    }

    @GetMapping("/permissions")
    @LogAnnotation(title = "菜单权限管理", action = "获取所有菜单权限")
    @RequiresPermissions("sys:permission:list")
    public DataResult<List<SysPermission>> getAllMenusPermission() {
        DataResult<List<SysPermission>> result = DataResult.success();
        result.setData(permissionService.selectAll());
        return result;
    }

    @GetMapping("/permission/tree")
    @LogAnnotation(title = "菜单权限管理", action = "获取所有目录菜单树")
    @RequiresPermissions(value = {"sys:permission:update", "sys:permission:add"}, logical = Logical.OR)
    public DataResult<List<PermissionRespNode>> getAllMenusPermissionTree(@RequestParam(required = false) String permissionId) {
        DataResult<List<PermissionRespNode>> result = DataResult.success();
        result.setData(permissionService.selectAllMenuByTree(permissionId));
        return result;
    }

    @GetMapping("/permission/allTree")
    @LogAnnotation(title = "菜单权限管理", action = "获取所有目录菜单树(过滤当前菜单)")
    @RequiresPermissions(value = {"sys:permission:list"}, logical = Logical.OR)
    public DataResult<List<PermissionRespNode>> getAllTree(@RequestParam(required = false) String filterMenuId) {
        DataResult<List<PermissionRespNode>> result = DataResult.success();
        result.setData(permissionService.getALLmenuTreeList(filterMenuId));
        return result;
    }

    @GetMapping("/permission/tree/all")
    @LogAnnotation(title = "菜单权限管理", action = "获取所有目录菜单树")
    @RequiresPermissions(value = {"sys:role:update", "sys:role:add"}, logical = Logical.OR)
    public DataResult<List<PermissionRespNode>> getAllPermissionTree() {
        DataResult<List<PermissionRespNode>> result = DataResult.success();
        result.setData(permissionService.selectAllByTree());
        return result;
    }

    @GetMapping("/permission/getChildrenIds")
    @LogAnnotation(title = "菜单权限管理", action = "获取所有子权限")
    @RequiresPermissions(value = {"sys:permission:list"}, logical = Logical.OR)
    public DataResult<List<String>> getChildrenIds(String permId) {
        DataResult<List<String>> result = DataResult.success();
        result.setData(permissionService.getChildrenIds(permId));
        return result;
    }
}
