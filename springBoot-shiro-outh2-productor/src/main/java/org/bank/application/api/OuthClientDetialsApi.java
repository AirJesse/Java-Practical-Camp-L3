package org.bank.application.api;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bank.common.util.DataResult;
import org.bank.domain.aop.annotation.LogAnnotation;
import org.bank.domain.entity.OuthClientDetails;
import org.bank.domain.service.OuthClientDetailsService;
import org.bank.domain.vo.req.OuthClientDetailsAddReqVO;
import org.bank.domain.vo.req.OuthClientDetailsPageReqVO;
import org.bank.domain.vo.resp.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/client")
public class OuthClientDetialsApi {
    @Autowired
    OuthClientDetailsService outhClientDetailsService;


    @PostMapping("/page")
    @RequiresPermissions("sys:client:list")
    @LogAnnotation(title = "客户端管理",action = "分页获取客户端列表")
    public DataResult<PageVO<OuthClientDetails>> pageInfo(@RequestBody OuthClientDetailsPageReqVO vo){
        DataResult<PageVO<OuthClientDetails>> result= DataResult.success();
        result.setData(outhClientDetailsService.pageInfo(vo));
        return result;
    }

    @PostMapping("/add")
    @RequiresPermissions("sys:client:add")
    @LogAnnotation(title = "客户端管理",action = "新增客户端")
    public DataResult addClient(@RequestBody @Valid OuthClientDetailsAddReqVO vo){
        outhClientDetailsService.addClient(vo);
        return DataResult.success();
    }

    @PutMapping("/update")
    @LogAnnotation(title = "客户端管理",action = "更新客户端信息")
    @RequiresPermissions("sys:client:update")
    public DataResult updateClientInfoById(@RequestBody @Valid OuthClientDetails vo){
        outhClientDetailsService.updateClient(vo);
        return DataResult.success();
    }

    @GetMapping("/{id}")
    @LogAnnotation(title = "客户端管理",action = "查询客户端详情")
    @RequiresPermissions("sys:client:detail")
    public DataResult<OuthClientDetails> detailInfo(@PathVariable("id") String id){
        DataResult<OuthClientDetails> result=DataResult.success();
        result.setData(outhClientDetailsService.detailInfo(id));
        return result;
    }

    @DeleteMapping("/delete/{id}")
    @LogAnnotation(title = "客户端管理",action = "删除客户端")
    @RequiresPermissions("sys:client:deleted")
    public DataResult deletedClient(@PathVariable("id") String id){
        outhClientDetailsService.deletedClient(id);
        return DataResult.success();
    }
}
