package org.bank.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @BelongsProject: springBoot-shiro-outh2
 * @BelongsPackage: org.bank.config
 * @Author: lizongle
 * @CreateTime: 2022-06-30  17:43
 * @Description: 跨域问题
 * @Version: 1.0
 */
@Configuration
public class webConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(1800)
                .allowedOrigins("*");
    }
}
