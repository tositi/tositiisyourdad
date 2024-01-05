package com.oddfar.campus.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.vo.user.RegisterVo;
import com.oddfar.campus.model.vo.user.SysUserVo;

import java.util.List;
import java.util.Map;

/**
 * @author zhiyuan
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 添加用户
     *
     * @param sysUser
     * @return
     */
    Boolean add(SysUser sysUser);

    /**
     * 注册
     * @param registerVo
     * @return
     */
    Map<String, Object> register(RegisterVo registerVo);

    /**
     * 用户列表（条件查询带分页）
     * @param pageParam
     * @param sysUserVo
     * @return
     */
    IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserVo sysUserVo);

    /**
     * 删除用户
     * @param uid
     * @return
     */
    Boolean delByUid(Long uid);

    /**
     * 批量删除用户
     * @param idList
     * @return
     */
    Boolean delByIds(Long[] idList);

    /**
     * 更改用户
     * 昵称 邮箱 账号 不可重复
     * @param sysUser
     * @return
     */
    Boolean update(SysUser sysUser);

    /**
     * 用户锁定
     *
     * @param userId
     * @param status 0：锁定 1：正常
     */
    Boolean lock(Long userId, Integer status);

    /**
     * 通过数据库获取登录的用户信息
     */
    SysUser getLoginUserInfo();

    /**
     * 根据 账号/邮箱 获取用户
     * @param account 账号/邮箱
     * @return
     */
    SysUser getByAccount(String account);

    /**
     * 根据 user_id 获取用户权限名
     * @param uid
     * @return
     */
    List<String> getRolesByUid(Long uid);

    /**
     * 根据 sysUser 获取用户URL头像
     * @param uid
     * @return
     */
    String getUserImageByUid(Long uid);

    /**
     * 根据 fileID 获取用户URL头像
     * @param fileId
     * @return
     */
    String getUserImageByAvatar(Long fileId);
    /**
     * 根据uid查询 user
     * @param uid
     * @return
     */
    SysUser queryUser(Long uid);

    /**
     * 判断用户是否登录
     */
    Boolean isLogin();

    /**
     * 判断 邮箱或者昵称 是否存在
     */
    Boolean isExist(SysUserVo sysUserVo);
}
