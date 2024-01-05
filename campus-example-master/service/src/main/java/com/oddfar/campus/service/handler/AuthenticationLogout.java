package com.oddfar.campus.service.handler;

import cn.hutool.core.util.ObjectUtil;
import com.google.gson.Gson;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.service.utils.TokenService;
import com.oddfar.campus.service.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销的时候处理情况
 */
@Component
public class AuthenticationLogout implements LogoutSuccessHandler {


    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    TokenService tokenService;
    Gson gson = new Gson();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = TokenUtil.getToken(request);
        String result = null;
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new CampusException("登录凭证不正确或者未登录,不能进行注销操作！", 3001);
        }
        try {
            //token正确,注销成功
            tokenService.delLoginUser(loginUser.getToken());
//            response.setHeader("token", token);  //在响应头添加token

            result = "注销成功";
            response.setContentType("application/json;charset=utf-8");  //设置编码格式
            response.getWriter().write(gson.toJson(Result.ok(result).ok(result)));    //返回给前端

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }
}
