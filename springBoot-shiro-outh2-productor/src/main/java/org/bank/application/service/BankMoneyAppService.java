package org.bank.application.service;

import lombok.extern.slf4j.Slf4j;
import org.bank.domain.entity.SysUser;
import org.bank.domain.repository.BankMoneyRepository;
import org.bank.domain.service.BankMoneyService;
import org.bank.domain.service.OAuthService;
import org.bank.domain.service.UserService;
import org.bank.domain.vo.resp.BankMoneyRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class BankMoneyAppService implements BankMoneyService {

    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private BankMoneyRepository bankMoneyRepository;
    @Autowired
    private UserService userService;



    @Override
    public List<BankMoneyRespVo> selectAll(String token) {
        SysUser sysUser = userService.detailInfo((String) oAuthService.getUsernameByAccessToken(token));

        List<BankMoneyRespVo> bankMoneyRespVos = bankMoneyRepository.selectListBankMoney(sysUser.getUsername());

        return bankMoneyRespVos;
    }
}
