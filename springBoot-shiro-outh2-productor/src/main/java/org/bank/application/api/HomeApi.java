package org.bank.application.api;

import lombok.extern.slf4j.Slf4j;
import org.bank.common.util.DataResult;
import org.bank.common.util.JwtTokenUtil;
import org.bank.domain.service.HomeService;
import org.bank.domain.vo.resp.HomeRespVO;
import org.bank.domain.vo.resp.HomeVueRespVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.controller
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequestMapping("/sys")
public class HomeApi {
    @Autowired
    private HomeService homeService;

    @GetMapping("/home")
    public DataResult<HomeRespVO> getHomeInfo(HttpServletRequest request){
        String accessToken=request.getHeader("authorization");
        /**
         * 通过access_token拿userId
         */
        String userId= JwtTokenUtil.getUserId(accessToken);
        DataResult<HomeRespVO> result=DataResult.success();
        result.setData(homeService.getHomeInfo(userId));
        return result;
    }
    @GetMapping("/homeForVue")
    public DataResult<HomeVueRespVO> getHomeForVueInfo(HttpServletRequest request){
        String accessToken=request.getHeader("authorization");
        /**
         * 通过access_token拿userId
         */
        String userId= JwtTokenUtil.getUserId(accessToken);
        DataResult<HomeVueRespVO> result=DataResult.success();
        result.setData(homeService.homeForVue(userId));
        log.info("返回结果为"+result.toString());
        return result;
    }
}
