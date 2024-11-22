package org.bank.domain.service;

import org.bank.domain.entity.OuthClientDetails;
import org.bank.domain.vo.req.OuthClientDetailsAddReqVO;
import org.bank.domain.vo.req.OuthClientDetailsPageReqVO;
import org.bank.domain.vo.resp.PageVO;

public interface OuthClientDetailsService {
    OuthClientDetails addClient(OuthClientDetailsAddReqVO vo);

    void updateClient(OuthClientDetails vo);

    OuthClientDetails detailInfo(String id);

    void deletedClient(String id);

    PageVO<OuthClientDetails> pageInfo(OuthClientDetailsPageReqVO vo);

    OuthClientDetails getClientForClientId(String clientId);

}
