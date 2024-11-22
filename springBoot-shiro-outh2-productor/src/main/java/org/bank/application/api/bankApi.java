package org.bank.application.api;


import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bank.common.util.DataResult;
import org.bank.domain.aop.annotation.LogAnnotation;
import org.bank.domain.entity.BankMoney;
import org.bank.domain.service.BankService;
import org.bank.domain.vo.req.BankMoneyAddReqVo;
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
@RequestMapping("/bank")
@RestController
public class bankApi {
    @Autowired
    private BankService bankService;

    @PostMapping("/add")
    @LogAnnotation(title = "新增银行余额", action = "新增银行余额")
    @RequiresPermissions("sys:bank:add")
    public DataResult<Boolean> add(@RequestBody @Valid BankMoneyAddReqVo vo) {
        DataResult<Boolean> result = DataResult.success();
        boolean add = bankService.add(vo);
        result.setData(add);
        return result;
    }

    @GetMapping("/myMoney")
    public DataResult<BankMoney> getCurrentUserBankMoney() {
        DataResult<BankMoney> result = DataResult.success();
        result.setData(bankService.getCurrentUserBankMoney());
        return result;
    }

    @PostMapping("/pageInfo")
    @LogAnnotation(title = "银行余额查询", action = "银行余额查询")
    @RequiresPermissions("sys:bank:list")
    public DataResult<List<BankMoney>> pageInfo(  ) {
        DataResult<List<BankMoney>> result = DataResult.success();
        result.setData(bankService.pageInfo());
        return result;
    }


}
