package com.lujiachao.login.controller;

import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.lujiachao.login.controller.vo.LoginRecordResponse;
import com.lujiachao.login.controller.vo.LoginRequest;
import com.lujiachao.login.entity.LoginCount;
import com.lujiachao.login.entity.User;
import com.lujiachao.login.service.LoginCountService;
import com.lujiachao.login.service.LoginService;
import com.lujiachao.login.service.UserService;
import com.lujiachao.login.utils.BeanCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author lujiachao
 * @since 2024-11-12
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginCountService loginCountService;

    @PostMapping("doLogin")
    public SaResult doLogin(@RequestBody @Valid LoginRequest loginRequest) {
        User user = userService.getUserForLogin(loginRequest.getUsername(), loginRequest.getPassword());
        if(user == null) {
            return SaResult.error("登入失败");
        }
        StpUtil.login(user.getId());
        return SaResult.ok("登入成功");

    }

    @GetMapping("isLogin")
    public String isLogin(String username, String password) {
        return "已登入：" + StpUtil.isLogin();
    }

}
