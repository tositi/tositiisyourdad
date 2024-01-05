package com.oddfar.campus.service.expander;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;

/**
 * 配置读取
 * 根据 Guns 代码修改
 *
 * @author zhiyuan
 */
public class ConfigContainer {
    private static final Dict CONFIG_CONTAINER = Dict.create();


    public static void putConfig(String key, Object value) {
        CONFIG_CONTAINER.put(key, value);
    }

    public static void deleteConfig(String key) {
        CONFIG_CONTAINER.remove(key);
    }


    public static <T> T getConfigValue(String configCode, Class<T> clazz) {
        String configValue = CONFIG_CONTAINER.getStr(configCode);
        return Convert.convert(clazz, configValue);
    }
}
