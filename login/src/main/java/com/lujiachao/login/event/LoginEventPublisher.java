package com.lujiachao.login.event;

import com.lujiachao.login.entity.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class LoginEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public LoginEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishLoginEvent(User user, String tokenId) {
        eventPublisher.publishEvent(new LoginEvent(this, user,tokenId));
    }
}
