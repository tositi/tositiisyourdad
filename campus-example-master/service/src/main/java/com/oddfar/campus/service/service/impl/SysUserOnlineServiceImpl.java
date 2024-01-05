package com.oddfar.campus.service.service.impl;

import com.oddfar.campus.common.constant.Constants;
import com.oddfar.campus.common.redis.RedisCache;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.service.service.SysUserOnlineService;
import com.oddfar.campus.service.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author zhiyuan
 */
@Service
public class SysUserOnlineServiceImpl implements SysUserOnlineService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private TokenService tokenService;

    @Override
    public void delByUid(Long uid) {
        //获取此uid的key列表
        Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + uid + "~*");
        for (String key : keys) {
            redisCache.deleteObject(key);

        }
    }

    @Override
    public void updateUser(SysUser sysUser) {
        Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + sysUser.getUid() + "~*");
        for (String key : keys) {
            LoginUser loginUser = redisCache.getCacheObject(key);
//            SysUser user = loginUser.getUser();
//            BeanUtil.copyProperties(sysUser, user);
            loginUser.setUser(sysUser);

            redisCache.setCacheObject(key, loginUser);
        }

    }
}
