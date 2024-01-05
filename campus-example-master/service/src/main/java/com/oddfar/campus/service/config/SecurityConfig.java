package com.oddfar.campus.service.config;


import com.oddfar.campus.service.filter.AuthenticationFilter;
import com.oddfar.campus.service.filter.MyAuthenticationFilter;
import com.oddfar.campus.service.handler.AuthenticationLogout;
import com.oddfar.campus.service.handler.TokenAccessDeniedHandler;
import com.oddfar.campus.service.handler.TokenAuthenticationEntryPoint;
import com.oddfar.campus.service.service.SysUserService;
import com.oddfar.campus.service.service.impl.SysUserServiceImpl;
import com.oddfar.campus.service.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * SpringSecurity 核心配置
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //java操作redis的string类型数据的类
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //注销处理器
    @Autowired
    AuthenticationLogout authenticationLogout;

    @Autowired
    private SysUserService userService;

    @Autowired
    private TokenService tokenService;

    //加密
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new SysUserServiceImpl();
    }


    /**
     * 认证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());

    }


    /**
     * 授权
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 定制请求的授权规则
        http.authorizeRequests()
                //所有人可访问
                .antMatchers("/filePreview/**").permitAll()
                .antMatchers("/tourist/**").permitAll()
                .antMatchers("/swagger*//**").permitAll()
                //admin superAdmin权限可访问
                .antMatchers("/admin/**").hasAnyRole("admin", "superAdmin")
                .antMatchers("/admin/config/**").hasAnyRole("superAdmin")
                .antMatchers("/admin/dict/**").hasAnyRole("superAdmin")
                .antMatchers("/api/operate/**").hasAnyRole("common", "admin", "superAdmin");


        http.cors().and().csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll()

                .and()
                .logout()
                .permitAll()
                .logoutSuccessHandler(authenticationLogout) //注销时的逻辑处理

                .and()
                .addFilter(new AuthenticationFilter(userService, authenticationManager(), stringRedisTemplate, tokenService))   //自定义认证过滤器
                .addFilter(new MyAuthenticationFilter(authenticationManager(), stringRedisTemplate, (SysUserServiceImpl) userDetailsService(), tokenService)) //自定义请求过滤器
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)     //去除默认的session、cookie

                .and()
                .exceptionHandling().authenticationEntryPoint(new TokenAuthenticationEntryPoint())  //未登录时的逻辑处理
                .accessDeniedHandler(new TokenAccessDeniedHandler());    //权限不足时的逻辑处理

    }


    /**
     * 用于解决跨域问题
     *
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }


}
