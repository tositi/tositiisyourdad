package com.oddfar.campus.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.campus.UserRole;

/**
 * @author zhiyuan
 */
public interface SysUserRoleService extends IService<UserRole> {

    /**
     * 根据 userType 添加用户权限
     * 目前写死 0-普通成员 1-管理员 2-系统管理员
     * @param sysUser
     */
    void addUserRole(SysUser sysUser);

    /**
     * 删除用户权限
     * @param uid
     */
    void delUserRole(long uid);



}
