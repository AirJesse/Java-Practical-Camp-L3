package org.bank.domain.repository;


import org.bank.domain.entity.BankMoney;

import java.util.List;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.domain.repository
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
public interface SysBankRepository {
    int deleteByPrimaryKey(String id);

    int insert(BankMoney record);

    List<BankMoney> selectAll( );


    BankMoney selectByPrimaryKey(String userId);

    BankMoney selectByUsername(String username);
}