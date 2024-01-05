package com.oddfar.campus.service.filter;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.service.service.SysUserService;
import com.oddfar.campus.service.utils.GetRequestJsonUtils;
import com.oddfar.campus.service.utils.TokenService;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义认证过滤器，判断认证成功还是失败，并给予相对应的逻辑处理
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    AuthenticationManager authenticationManager;
    SysUserService userService;
    StringRedisTemplate stringRedisTemplate;
    TokenService tokenService;

    Gson gson = new Gson();

    public AuthenticationFilter(SysUserService userService, AuthenticationManager authenticationManager, StringRedisTemplate stringRedisTemplate, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.stringRedisTemplate = stringRedisTemplate;
        this.tokenService = tokenService;
    }

    // 未认证时调用此方法，判断认证是否成功
    // 认证成功与否由 authenticate() 去判断
    // 我们在这里只负责传递所需要的参数即可
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //获取post的 json 数据
        JSONObject json = GetRequestJsonUtils.getRequestJsonObject(request);
        String username = (String) json.get("username");
        String password = (String) json.get("password");

        //如果是 post 请求数据 用以下方法获取
//        String password = request.getParameter("password");
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));

        return authenticate;
    }

    //验证成功操作
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authentication) throws IOException {
        //验证成功则向redis缓存写入token，然后在响应头添加token，并向前端返回
        //request.getParameter("username") 也可获取登录的账号
        SysUser sysUser = userService.getByAccount(authentication.getName());
        sysUser.setImageUrl(userService.getUserImageByAvatar(sysUser.getAvatar()));
        Long uid = sysUser.getUid();
        //获取token
//        String token = JwtHelper.createToken(uid, sysUser.getUserAccount(), UUID.randomUUID().toString());
        LoginUser loginUser = getLoginUser(sysUser);
        String token = tokenService.createToken(loginUser);

//        response.setHeader("token", token);  //在响应头添加token

        //登录成功：返回页面显示名称
        Map<String, Object> map = new HashMap<>();

        map.put("uid", uid.toString());
        map.put("name", sysUser.getUserName());
        map.put("token", token);
        map.put("image", userService.getUserImageByUid(uid));

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(gson.toJson(Result.ok(map)));
    }

    //验证失败
    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        Result<String> msg = Result.fail(failed.getMessage());

        response.getWriter().write(gson.toJson(msg));

    }

    private LoginUser getLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        loginUser.setUserId(user.getUid());
        loginUser.setLoginTime(System.currentTimeMillis());
        //设置权限
        loginUser.setPermissions(userService.getRolesByUid(user.getUid()));


        return loginUser;
    }
}
