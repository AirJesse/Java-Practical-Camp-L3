package org.bank.domain.service;


import org.bank.domain.vo.resp.BankMoneyRespVo;

import java.util.List;


public interface BankMoneyService {



    List<BankMoneyRespVo> selectAll(String token);
}
