package com.oddfar.campus.service.cepter;

import cn.hutool.core.util.ObjectUtil;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.common.service.JwtHelper;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.service.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhiyuan
 */
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = request.getHeader("token");

        if (ObjectUtil.isEmpty(cookies) && ObjectUtil.isEmpty(token)) {
            throw new CampusException("请先登录", 3001);
        }

        if (ObjectUtil.isEmpty(token)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        }

        if (StringUtils.isEmpty(token)) {
            throw new CampusException("请先登录", 3001);
        }
        Long userId = JwtHelper.getUserId(token);
        if (StringUtils.isEmpty(userId)) {
            throw new CampusException("token错误，请重新登录", 3001);
        }
//        redisTemplate.hasKey(userId.toString());//key是否存在
        //从redis获取token
        String redis_token = redisTemplate.opsForValue().get(userId.toString());
        if (StringUtils.isEmpty(redis_token)) {
            throw new CampusException("token过期，请重新登录", 3001);
        }

        if (!redis_token.equals(token)) {
            throw new CampusException("token不一致，请重新登录", 3001);
        }
        //判断用户状态
        SysUser userInfo = sysUserService.getById(userId);
        if (userInfo.getStatus() == 0) {
            throw new CampusException("用户已被锁定，禁止操作", 3001);
        }

        //重新更新redis有效期
//        redisTemplate.expire(userId.toString(), 5, TimeUnit.DAYS);

        return true;
    }


}
