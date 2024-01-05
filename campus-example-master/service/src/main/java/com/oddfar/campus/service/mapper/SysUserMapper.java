package com.oddfar.campus.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oddfar.campus.model.campus.Role;
import com.oddfar.campus.model.campus.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhiyuan
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {


    /**
     * 获取指定id用户的权限
     *
     * @param uid
     * @return
     */
    List<Role> getUserRolesById(Long uid);



}
