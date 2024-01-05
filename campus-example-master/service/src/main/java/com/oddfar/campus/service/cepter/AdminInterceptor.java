package com.oddfar.campus.service.cepter;//具体拦截器的业务类

import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.common.service.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//具体拦截器的业务类
public class AdminInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //在请求处理之前进行调用: Controller方法调用之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String token = request.getHeader("X-Token");
        if (!StringUtils.isEmpty(token)) {
            if (token.equals("123456")) {
                return true;
            }
            String userName = JwtHelper.getUserName(token);
            Long userId = JwtHelper.getUserId(token);

            //从redis获取token
//            String redis_token = redisTemplate.opsForValue().get(userId.toString());
//            if (StringUtils.isEmpty(redis_token)) {
//                throw new CampusException("token过期，请重新登录", 3001);
//            }
//
//            if (!redis_token.equals(token)) {
//                throw new CampusException("token不一致，请重新登录", 3001);
//            }
            String requestURI = request.getRequestURI();
            if (requestURI.contains("/admin/ad/") && userId != 1) {
                throw new CampusException("此权限需要超级管理员", 3001);
            }

            //重新更新redis有效期
//            redisTemplate.expire(userId.toString(), 5, TimeUnit.DAYS);
            return true;
        } else {
            //如果false，停止流程，api被拦截
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            PrintWriter printWriter = response.getWriter();
//            printWriter.write(JSONObject.toJSONString(Result.fail("请先登录")));
            throw new CampusException("请先登录", 2001);
//            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//        System.out.println("postHandle被调用");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        System.out.println("afterCompletion被调用");
    }
}