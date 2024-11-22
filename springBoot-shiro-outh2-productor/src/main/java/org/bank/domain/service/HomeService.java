package org.bank.domain.service;


import org.bank.domain.vo.resp.HomeRespVO;
import org.bank.domain.vo.resp.HomeVueRespVO;

public interface HomeService {

    HomeRespVO getHomeInfo(String userId);
    HomeVueRespVO homeForVue(String userId);
}
