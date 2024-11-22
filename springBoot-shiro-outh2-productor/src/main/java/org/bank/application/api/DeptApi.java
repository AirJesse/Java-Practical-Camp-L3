package org.bank.application.api;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bank.common.util.DataResult;
import org.bank.domain.aop.annotation.LogAnnotation;
import org.bank.domain.entity.SysDept;
import org.bank.domain.entity.SysUser;
import org.bank.domain.service.DeptService;
import org.bank.domain.vo.req.DeptAddReqVO;
import org.bank.domain.vo.req.DeptPageReqVO;
import org.bank.domain.vo.req.DeptUpdateReqVO;
import org.bank.domain.vo.req.UserPageUserByDeptReqVO;
import org.bank.domain.vo.resp.DeptRespNodeVO;
import org.bank.domain.vo.resp.PageVO;
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
public class DeptApi {
    @Autowired
    private DeptService deptService;

    @PostMapping("/dept")
    @LogAnnotation(title = "机构管理",action = "新增组织")
    @RequiresPermissions("sys:dept:add")
    public DataResult<SysDept> addDept(@RequestBody @Valid DeptAddReqVO vo){
        DataResult<SysDept> result=DataResult.success();
        result.setData(deptService.addDept(vo));
        return result;
    }

    @DeleteMapping("/dept/{id}")
    @LogAnnotation(title = "机构管理",action = "删除组织")
    @RequiresPermissions("sys:dept:deleted")
    public DataResult deleted(@PathVariable("id") String id){
        deptService.deleted(id);
        return DataResult.success();
    }

    @PostMapping("/dept/delete")
    @LogAnnotation(title = "机构管理",action = "删除组织")
    @RequiresPermissions("sys:dept:deleted")
    public DataResult batchDeleted(@RequestBody List<String> ids){
        deptService.deleted(ids);
        return DataResult.success();
    }

    @PutMapping("/dept")
    @LogAnnotation(title = "机构管理",action = "更新组织信息")
    @RequiresPermissions("sys:dept:update")
    public DataResult updateDept(@RequestBody @Valid DeptUpdateReqVO vo){
        deptService.updateDept(vo);
        return DataResult.success();
    }

    @GetMapping("/dept/{id}")
    @LogAnnotation(title = "机构管理",action = "查询组织详情")
    @RequiresPermissions("sys:dept:detail")
    public DataResult<SysDept> detailInfo(@PathVariable("id") String id){
        DataResult<SysDept> result=DataResult.success();
        result.setData(deptService.detailInfo(id));
        return result;
    }

    @PostMapping("/depts")
    @LogAnnotation(title = "机构管理",action = "分页获取组织信息")
    @RequiresPermissions("sys:dept:list")
    public DataResult<PageVO<SysDept>> pageInfo(@RequestBody DeptPageReqVO vo){
        DataResult<PageVO<SysDept>> result=DataResult.success();
        result.setData(deptService.pageInfo(vo));
        return result;
    }

    @PostMapping("/deptForVue")
    @LogAnnotation(title = "机构管理",action = "vue页面获取组织信息")
    @RequiresPermissions("sys:dept:list")
    public DataResult<PageVO<DeptRespNodeVO>> pageInfoForVue(@RequestBody DeptPageReqVO vo){
        DataResult<PageVO<DeptRespNodeVO>> result=DataResult.success();
        result.setData(deptService.pageInfoForVue(vo));
        return result;
    }

    @GetMapping("/dept/tree")
    @LogAnnotation(title = "机构管理",action = "树型组织列表")
    @RequiresPermissions(value = {"sys:user:update","sys:user:add","sys:dept:add","sys:dept:update"},logical = Logical.OR)
    public DataResult<List<DeptRespNodeVO>> getTree(@RequestParam(required = false) String deptId){
        DataResult<List<DeptRespNodeVO>> result=DataResult.success();
        result.setData(deptService.deptTreeList(deptId));
        return result;
    }

    @GetMapping("/dept/allTree")
    @LogAnnotation(title = "机构管理",action = "树型组织列表")
    @RequiresPermissions(value = {"sys:user:update","sys:user:add","sys:dept:add","sys:dept:update"},logical = Logical.OR)
    public DataResult<List<DeptRespNodeVO>> getAllTree(@RequestParam(required = false) String filterDeptId){
        DataResult<List<DeptRespNodeVO>> result=DataResult.success();
        result.setData(deptService.getALLdeptTreeList(filterDeptId));
        return result;
    }

    @PostMapping("/dept/users")
    @LogAnnotation(title = "机构管理",action = "分页获取组织下所有用户")
    @RequiresPermissions("sys:dept:user:list")
    public DataResult<PageVO<SysUser>> pageDeptUserInfos(@RequestBody @Valid UserPageUserByDeptReqVO vo){
        DataResult<PageVO<SysUser>> result=DataResult.success();
        result.setData(deptService.pageDeptUserInfo(vo));
        System.out.println("fsdfds");
        return result;
    }

    @GetMapping("/depts")
    @LogAnnotation(title = "机构管理",action = "获取所有组织机构")
    @RequiresPermissions("sys:dept:list")
    public DataResult<List<SysDept>> getDeptAll(){
        DataResult<List<SysDept>> result=DataResult.success();
        result.setData(deptService.selectAll());
        return result;
    }
}
