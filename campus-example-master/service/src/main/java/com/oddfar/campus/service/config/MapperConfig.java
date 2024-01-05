package com.oddfar.campus.service.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.oddfar.campus.service.mapper")
public class MapperConfig {

}
