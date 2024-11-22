package com.lujiachao.login.event;

import com.lujiachao.login.entity.User;
import org.springframework.context.ApplicationEvent;

public class LoginEvent extends ApplicationEvent {
    private  User user;

    public LoginEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
