package org.bank.domain.repository;


import org.bank.domain.entity.OuthClientDetails;
import org.bank.domain.vo.req.OuthClientDetailsPageReqVO;

import java.util.List;

public interface OuthClientDetailsRepository {
    int deleteByPrimaryKey(String id);

    int insert(OuthClientDetails record);

    int insertSelective(OuthClientDetails record);

    OuthClientDetails selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OuthClientDetails record);

    int updateByPrimaryKey(OuthClientDetails record);

    List<OuthClientDetails> selectAll(OuthClientDetailsPageReqVO vo);

    OuthClientDetails selectByClientId (String clientId);

    OuthClientDetails selectByClientName (String clientName);

}
