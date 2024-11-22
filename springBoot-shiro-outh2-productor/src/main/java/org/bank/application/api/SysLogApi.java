package org.bank.application.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bank.common.util.DataResult;
import org.bank.domain.aop.annotation.LogAnnotation;
import org.bank.domain.entity.SysLog;
import org.bank.domain.service.LogService;
import org.bank.domain.vo.req.SysLogPageReqVO;
import org.bank.domain.vo.resp.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class SysLogApi {

    @Autowired
    private LogService logService;

    @PostMapping("/logs")
    @LogAnnotation(title = "系统操作日志管理",action = "分页查询系统操作日志")
    @RequiresPermissions("sys:log:list")
    public DataResult<PageVO<SysLog>> pageInfo(@RequestBody SysLogPageReqVO vo){
        PageVO<SysLog> sysLogPageVO = logService.pageInfo(vo);
        DataResult<PageVO<SysLog>> result= DataResult.success();
        result.setData(sysLogPageVO);
        return result;
    }

    @DeleteMapping("/logs")
    @LogAnnotation(title = "系统操作日志管理",action = "删除系统操作日志")
    @RequiresPermissions("sys:log:deleted")
    public DataResult deleted(@RequestBody List<String> logIds){
        logService.deleted(logIds);
        return DataResult.success();
    }
}
