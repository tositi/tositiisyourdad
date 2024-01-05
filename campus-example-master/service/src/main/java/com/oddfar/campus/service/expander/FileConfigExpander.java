package com.oddfar.campus.service.expander;

import com.oddfar.campus.service.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhiyuan
 */
public class FileConfigExpander {
    @Autowired
    SysConfigService configService;

    /**
     * 默认存储的bucket名称
     */
    public static String getDefaultBucket() {
        return ConfigContainer.getConfigValue("sys.file.default.bucket", String.class);

    }




}
