package com.oddfar.campus.service.filter;

import cn.hutool.core.util.ObjectUtil;
import com.google.gson.Gson;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.service.service.impl.SysUserServiceImpl;
import com.oddfar.campus.service.utils.TokenService;
import com.oddfar.campus.service.utils.TokenUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义请求过滤器，token没有或者不正确的时候，
 * 告诉用户执行相应操作，token正确且认证的情况下则放行请求，
 * 交由认证过滤器进行认证操作
 */
public class MyAuthenticationFilter extends BasicAuthenticationFilter {

    StringRedisTemplate stringRedisTemplate;
    SysUserServiceImpl userServiceImpl;
    TokenService tokenService;
    Gson gson = new Gson();

    public MyAuthenticationFilter(AuthenticationManager authenticationManager, StringRedisTemplate stringRedisTemplate, SysUserServiceImpl userServiceImpl, TokenService tokenService) {
        super(authenticationManager);
        this.stringRedisTemplate = stringRedisTemplate;
        this.userServiceImpl = userServiceImpl;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String token = TokenUtil.getToken(request);
            if (StringUtils.isEmpty(token)) {
                //token为空，放行
                chain.doFilter(request, response);
                return;
            }

            LoginUser loginUser = tokenService.getLoginUser(request);
            if (ObjectUtil.isEmpty(loginUser)) {
                throw new CampusException("登录凭证不正确或者超时了,请重新登录！-1", 3001);
            }

            Collection<? extends GrantedAuthority> authorities = getAuthorities(loginUser.getPermissions());

            //存入SecurityContextHolder
            //获取权限信息封装到Authentication中
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //放行
//            chain.doFilter(request, response);


        } catch (CampusException e) {
            //抛出异常，并返回给前端
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            Result<String> msg = Result.fail(e.getLocalizedMessage());
            response.getWriter().write(gson.toJson(msg));
            response.getWriter().flush();
//            chain.doFilter(request, response);
            return;
        }


        super.doFilterInternal(request, response, chain);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> permissions) {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        //把permissions中String类型的权限信息封装成SimpleGrantedAuthority对象
//       authorities = new ArrayList<>();
//        for (String permission : permissions) {
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
//            authorities.add(authority);
//        }
        authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }
}
