package org.bank.application.api;

import org.bank.domain.service.BankMoneyService;
import org.bank.domain.vo.resp.BankMoneyRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供其他服务调用
 *
 * @author zhaoaiping
 * @date 2022/8/24 16:29
 */
@RequestMapping("/bank-other")
@RestController
public class BankOtherApi {
    @Autowired
    BankMoneyService bankMoneyService;

    @GetMapping("/findByToken")
    public List<BankMoneyRespVo> findByToken(String token) {
        List<BankMoneyRespVo> bankMoneyRespVos = bankMoneyService.selectAll(token);
        return bankMoneyRespVos;
    }
}
