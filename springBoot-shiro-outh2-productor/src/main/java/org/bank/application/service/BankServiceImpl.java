package org.bank.application.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.bank.domain.entity.BankMoney;
import org.bank.domain.entity.SysUser;
import org.bank.domain.repository.SysBankRepository;
import org.bank.domain.service.BankService;
import org.bank.domain.service.OAuthService;
import org.bank.domain.service.UserService;
import org.bank.domain.vo.req.BankMoneyAddReqVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@Slf4j
public class BankServiceImpl implements BankService {

    @Autowired
    SysBankRepository sysBankRepository;

    @Autowired
    OAuthService oAuthService;

    @Autowired
    UserService userService;


    @Override
    public boolean add(BankMoneyAddReqVo vo) {
        BankMoney bankMoney = new BankMoney();
        BeanUtils.copyProperties(vo, bankMoney);
        int insert = sysBankRepository.insert(bankMoney);
        if (insert > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<BankMoney> pageInfo() {

        return sysBankRepository.selectAll();
    }

    @Override
    public BankMoney getCurrentUserBankMoney() {

        String accessToken = (String) SecurityUtils.getSubject().getPrincipal();
        String userId = oAuthService.getUserIdByAccessToken(accessToken);
        if (StringUtils.isEmpty(userId)) {
            return null;
        }
        SysUser user = userService.detailInfo(userId);
        if (StringUtils.isEmpty(user)) {
            return null;
        }
        return sysBankRepository.selectByUsername(user.getUsername());
    }
}
