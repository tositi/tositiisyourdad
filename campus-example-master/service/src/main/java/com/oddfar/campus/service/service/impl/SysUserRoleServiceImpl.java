package com.oddfar.campus.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.campus.UserRole;
import com.oddfar.campus.service.mapper.SysUserRoleMapper;
import com.oddfar.campus.service.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author zhiyuan
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, UserRole> implements SysUserRoleService {


    @Override
    public void addUserRole(SysUser sysUser) {
        //先删除
        this.delUserRole(sysUser.getUid());

        UserRole userRole = new UserRole();
        long role = 3L;

        switch (sysUser.getUserType()) {
            case 0://用户
                role = 3L;
                break;
            case 1://管理员
                role = 2L;
                break;
            case 2://系统管理员
                role = 1L;
                break;
            default:
                role = 3L;
                break;
        }
        userRole.setUserId(sysUser.getUid());
        userRole.setRoleId(role);
        this.save(userRole);

    }

    @Override
    public void delUserRole(long uid) {
        //先删除
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", uid);
        this.remove(wrapper);
    }


}
