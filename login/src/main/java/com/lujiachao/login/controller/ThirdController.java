package com.lujiachao.login.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.lujiachao.login.common.Constant;
import com.lujiachao.login.common.ResultBody;
import com.lujiachao.login.thrid.ThirdService;

import com.lujiachao.login.thrid.dto.BankMoney;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方服务
 */

@RestController
@RequestMapping("/third")
@Slf4j
public class ThirdController {

    @Autowired
    private ThirdService thirdService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/myMoney")
    public ResultBody getMyMoney(String code) {
        int userId = Integer.parseInt((String) StpUtil.getLoginId());
        String accessToken = (String) redisTemplate.opsForValue().get(Constant.BANK_ACCESS_TOKEN_KEY + userId);
        if (StringUtils.isBlank(code) && StringUtils.isBlank(accessToken)) {
            return new ResultBody(201, "无第三方授权");
        }
        if (StringUtils.isNotBlank(accessToken)) {
            BankMoney myMoney = thirdService.getMyMoney(accessToken);
            return new ResultBody(myMoney);
        }
        accessToken = thirdService.getBankAccessToken(code, userId);
        return new ResultBody(thirdService.getMyMoney(accessToken));
    }

    @PostMapping("/unBindBank")
    public ResultBody unBindBank(){
        int userId = Integer.parseInt((String) StpUtil.getLoginId());
        redisTemplate.delete(Constant.BANK_ACCESS_TOKEN_KEY + userId);
        return new ResultBody(200, "解绑成功");
    }
}
