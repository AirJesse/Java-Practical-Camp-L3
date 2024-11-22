package org.bank.application.service;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.bank.common.exception.BusinessException;
import org.bank.common.exception.code.BaseResponseCode;
import org.bank.common.util.PageUtils;
import org.bank.common.util.PasswordUtils;
import org.bank.domain.entity.OuthClientDetails;
import org.bank.domain.repository.OuthClientDetailsRepository;
import org.bank.domain.service.OuthClientDetailsService;
import org.bank.domain.vo.req.OuthClientDetailsAddReqVO;
import org.bank.domain.vo.req.OuthClientDetailsPageReqVO;
import org.bank.domain.vo.resp.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class OuthClientDetailsAppService implements OuthClientDetailsService {
    @Autowired
    private OuthClientDetailsRepository outhClientDetailsRepository;
    @Override
    public OuthClientDetails addClient(OuthClientDetailsAddReqVO vo) {
        OuthClientDetails outhClientDetails=null;
        //校验客户端是否存在
        String clientName=vo.getClientName();
        outhClientDetails=outhClientDetailsRepository.selectByClientName(clientName);
        if(outhClientDetails==null||StringUtils.isEmpty(outhClientDetails.getId())){
            outhClientDetails=new OuthClientDetails();
        }else{
            throw new BusinessException(BaseResponseCode.CLIENT_ISEXIT);
        }
        BeanUtils.copyProperties(vo,outhClientDetails);
        String encode = PasswordUtils.encode(vo.getClientSecret(), "ClientSecret");
        outhClientDetails.setClientSecret(encode);
        String client_Id=UUID.randomUUID().toString();
        outhClientDetails.setId(client_Id);
        outhClientDetails.setClientId(client_Id);
        int i = outhClientDetailsRepository.insertSelective(outhClientDetails);
        if (i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return outhClientDetails;
    }

    @Override
    public void updateClient(OuthClientDetails vo) {
        //没有ID直接结束
        if(StringUtils.isEmpty(vo.getId())||StringUtils.isEmpty(vo.getClientId())){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        int i = outhClientDetailsRepository.updateByPrimaryKeySelective(vo);
        if (i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public OuthClientDetails detailInfo(String id) {
        return outhClientDetailsRepository.selectByPrimaryKey(id);
    }

    @Override
    public void deletedClient(String id) {
        int i= outhClientDetailsRepository.deleteByPrimaryKey(id);
        if (i==0){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public PageVO<OuthClientDetails> pageInfo(OuthClientDetailsPageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<OuthClientDetails> outhClientDetailsList = outhClientDetailsRepository.selectAll(vo);
        return PageUtils.getPageVO(outhClientDetailsList);
    }

    @Override
    public OuthClientDetails getClientForClientId(String clientId) {
        return outhClientDetailsRepository.selectByClientId(clientId);
    }
}
