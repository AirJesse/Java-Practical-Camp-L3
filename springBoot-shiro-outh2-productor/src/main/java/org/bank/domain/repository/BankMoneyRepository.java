package org.bank.domain.repository;


import org.apache.ibatis.annotations.Param;
import org.bank.domain.vo.resp.BankMoneyRespVo;

import java.util.List;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.repository
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
public interface BankMoneyRepository {


    List<BankMoneyRespVo> selectListBankMoney(@Param("username") String  username);

}