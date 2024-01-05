package com.oddfar.campus.service.service;

import com.oddfar.campus.model.campus.SysUser;

/**
 * 在线用户
 *
 * @author zhiyuan
 */
public interface SysUserOnlineService {
    /**
     * 删除缓存用户
     * @param uid
     */
    void delByUid(Long uid);

    /**
     * 修改缓存用户
     */
    void updateUser(SysUser sysUser);
}
