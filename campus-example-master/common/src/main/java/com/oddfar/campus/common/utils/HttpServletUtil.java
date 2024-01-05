package com.oddfar.campus.common.utils;

import com.oddfar.campus.common.exception.CampusException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhiyuan
 */
public class HttpServletUtil {
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new CampusException("获取不到http context，请确认当前请求是http请求", 2001);
        } else {
            return requestAttributes.getRequest();
        }
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new CampusException("获取不到http context，请确认当前请求是http请求", 2001);
        } else {
            return requestAttributes.getResponse();
        }
    }
}
