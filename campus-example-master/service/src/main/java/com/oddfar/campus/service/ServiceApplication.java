package com.oddfar.campus.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author zhiyuan
 */
@EnableAsync //开启异步注解功能
@SpringBootApplication
@ComponentScan(basePackages = "com.oddfar")
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
        System.out.println("项目启动成功....");
    }

}
