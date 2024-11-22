package org.bank.domain.service;


import org.bank.domain.entity.SysLog;
import org.bank.domain.vo.req.SysLogPageReqVO;
import org.bank.domain.vo.resp.PageVO;

import java.util.List;


public interface LogService {

    PageVO<SysLog> pageInfo(SysLogPageReqVO vo);

    void deleted(List<String> logIds);
}
