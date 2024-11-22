package org.bank.domain.service;




import org.bank.domain.entity.BankMoney;
import org.bank.domain.vo.req.BankMoneyAddReqVo;

import java.util.List;

public interface BankService {

    boolean add(BankMoneyAddReqVo vo);


    List<BankMoney> pageInfo( );


}
