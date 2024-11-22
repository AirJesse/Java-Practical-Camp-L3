package org.bank.application.service;

import com.github.pagehelper.PageHelper;
import org.bank.common.util.PageUtils;
import org.bank.domain.entity.SysLog;
import org.bank.domain.repository.SysLogRepository;
import org.bank.domain.service.LogService;
import org.bank.domain.vo.req.SysLogPageReqVO;
import org.bank.domain.vo.resp.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LogAppService implements LogService {
    @Autowired
    private SysLogRepository sysLogMapper;

    @Override
    public PageVO<SysLog> pageInfo(SysLogPageReqVO vo) {

        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<SysLog> sysLogs = sysLogMapper.selectAll(vo);
        return PageUtils.getPageVO(sysLogs);
    }

    @Override
    public void deleted(List<String> logIds) {
        sysLogMapper.batchDeletedLog(logIds);
    }
}
