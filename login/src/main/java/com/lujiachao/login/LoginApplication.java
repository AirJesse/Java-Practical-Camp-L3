package com.lujiachao.login;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan("com.lujiachao.login.mapper")
public class LoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }

}
