package com.lujiachao.login.config;

import com.lujiachao.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private UserService userService;

    private String exclusions = "/login,/oauth/token,/**/*.html,/swagger-resources/**,/swagger-ui.html,/**/*.js," +
            "/**/*.css,/**/*.jpg,/**/*.png,/**/*.ttf";


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }


}
