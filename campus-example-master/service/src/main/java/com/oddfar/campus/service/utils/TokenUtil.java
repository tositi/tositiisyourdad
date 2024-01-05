package com.oddfar.campus.service.utils;

import cn.hutool.core.util.ObjectUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zhiyuan
 */
public class TokenUtil {
    /**
     * 从 request 获取头信息的token，无则从cookie里获取
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = request.getHeader("token");

        if (ObjectUtil.isEmpty(token)) {

            if (ObjectUtil.isEmpty(cookies)) {
                return null;
            }
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        }

        return token;

    }

}
