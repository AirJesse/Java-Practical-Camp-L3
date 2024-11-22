package com.lujiachao.login.listener;

import cn.dev33.satoken.stp.StpUtil;
import com.lujiachao.login.entity.Login;
import com.lujiachao.login.entity.User;
import com.lujiachao.login.event.LoginEvent;
import com.lujiachao.login.service.LoginCountService;
import com.lujiachao.login.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class LoginEventListener {
    @Autowired
    private LoginCountService loginCountService;
    @Autowired
    private LoginService loginService;

    @EventListener
    @Transactional
    public void onLoginEvent(LoginEvent event) {
        User user = event.getUser();
        log.info("用户已登入：{}", user.getUserName());
        Login login = new Login();
        login.setUserName(user.getUserName());
        login.setTokenId(StpUtil.getTokenValue());
        loginService.updateToken(login);
    }
}
