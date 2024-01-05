package com.oddfar.campus.service.cepter;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration 先禁用
public class MyWebConfig implements WebMvcConfigurer {

    @Bean
    UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }

    @Bean
    AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AdminInterceptor()).addPathPatterns("/admin/**");
//        registry.addInterceptor(adminInterceptor()).addPathPatterns("/admin/**");
//
//        registry.addInterceptor(userInterceptor()).addPathPatterns("/api/operate/**");
//
//        WebMvcConfigurer.super.addInterceptors(registry);
    }
}