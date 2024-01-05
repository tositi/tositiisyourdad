package com.oddfar.campus.service.controller.admin;


import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.common.utils.HttpServletUtil;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.service.mapper.SysUserMapper;
import com.oddfar.campus.service.service.SysUserService;
import com.oddfar.campus.service.utils.TokenService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping
@Api(tags = "管理员管理")
public class AdminInfoController {


    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    TokenService tokenService;


    @GetMapping(value = "/admin/info")
    public Result info() {
//        SysUser user = sysUserService.getLoginUserInfo();
        LoginUser loginUser = tokenService.getLoginUser(HttpServletUtil.getRequest());

        SysUser user = loginUser.getUser();

        HashMap<String, Object> responseData = new HashMap<>();

        responseData.put("roles", loginUser.getPermissions());
        responseData.put("name", user.getUserName());
        responseData.put("avatar", user.getImageUrl());

        return Result.ok(responseData);
    }
}
