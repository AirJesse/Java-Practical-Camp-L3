package com.lujiachao.login.event;

import com.lujiachao.login.entity.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LoginEvent extends ApplicationEvent {
    private User user;
    private String tokenId;

    public LoginEvent(Object source, User user, String tokenId) {
        super(source);
        this.user = user;
        this.tokenId = tokenId;
    }

}
