package com.oddfar.campus.service.utils;

import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.model.user.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全服务工具类,根据若依代码修改
 *
 * @author zhiyuan
 */
public class SecurityUtils {

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {

            throw new CampusException("获取用户信息异常", 3002);
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
