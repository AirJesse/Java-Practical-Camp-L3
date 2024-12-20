package com.lujiachao.login.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.lujiachao.login.controller.vo.LoginRecordResponse;
import com.lujiachao.login.entity.LoginCount;
import com.lujiachao.login.service.UserService;
import com.lujiachao.login.service.LoginCountService;
import com.lujiachao.login.service.LoginService;
import com.lujiachao.login.utils.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户token信息表 前端控制器
 * </p>
 *
 * @author lujiachao
 * @since 2024-11-12
 */
@RestController
@RequestMapping("/login")
public class LoginController {


    @Autowired
    private LoginCountService loginCountService;


    @GetMapping("info")
    public List<LoginRecordResponse> findAllLoginRecord() {

        List<LoginCount> list = loginCountService.list();
        return BeanCopyUtil.copyList(list, LoginRecordResponse.class);
    }
}
