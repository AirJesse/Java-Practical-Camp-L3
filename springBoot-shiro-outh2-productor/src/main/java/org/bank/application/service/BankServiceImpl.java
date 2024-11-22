package org.bank.application.service;

import lombok.extern.slf4j.Slf4j;
import org.bank.domain.entity.BankMoney;
import org.bank.domain.repository.SysBankRepository;
import org.bank.domain.service.BankService;
import org.bank.domain.vo.req.BankMoneyAddReqVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class BankServiceImpl implements BankService {

    @Autowired
    SysBankRepository sysBankRepository;


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
    public List<BankMoney> pageInfo( ) {

        return sysBankRepository.selectAll();
    }
}
