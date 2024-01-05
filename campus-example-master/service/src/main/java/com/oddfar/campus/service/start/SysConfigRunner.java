package com.oddfar.campus.service.start;


import com.oddfar.campus.service.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 实现ApplicationRunner接口,执行顺序按照value值决定,值小先执行
 */
@Slf4j
@Component
@Order(value = 1)
public class SysConfigRunner implements ApplicationRunner {

    @Autowired
    private SysConfigService sysConfigService;

    @Override
    public void run(ApplicationArguments args) {
        sysConfigService.loadingConfigCache();
        log.info("加载参数缓存数据到redis");

    }
}
