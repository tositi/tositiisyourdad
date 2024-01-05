package com.oddfar.campus.service.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.model.vo.campus.FileInfoVo;
import com.oddfar.campus.model.vo.user.RegisterVo;
import com.oddfar.campus.model.vo.user.SysUserVo;
import com.oddfar.campus.service.service.FileInfoService;
import com.oddfar.campus.service.service.SysUserService;
import com.oddfar.campus.service.service.impl.SysUserServiceImpl;
import com.oddfar.campus.service.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhiyuan
 */
@RestController
@RequestMapping("/admin/user")
@Api(tags = "用户管理")
public class UserSetController {
    @Autowired
    private SysUserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private FileInfoService fileInfoService;

    //用户列表（条件查询带分页）
    @ApiOperation(value = "分页查询")
    @PostMapping("{page}/{limit}")
    public Result list(@PathVariable Long page, @PathVariable Long limit,
                       @RequestBody(required = false) SysUserVo sysUserVo) {

        Page<SysUser> pageParam = new Page<>(page, limit);

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();

        if (user.getUserType() != 2) {
            //非超级管理员只能查看普通用户信息
            sysUserVo.setUserType(0);
        }

        IPage<SysUser> pageModel = userService.selectPage(pageParam, sysUserVo);

        return Result.ok(pageModel);
    }


    //锁定用户或取消锁定
    @ApiOperation(value = "锁定")
    @PutMapping("lock/{uid}/{status}")
    public Result lock(@PathVariable("uid") Long uid, @PathVariable("status") Integer status) {
        userService.lock(uid, status);
        return Result.ok();
    }



    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{userIds}")
    public Result del(@PathVariable Long[] userIds) {

        userService.delByIds(userIds);

        return Result.ok();

    }


    @ApiOperation(value = "会员添加")
    @PostMapping("/add")
    public Result add(@RequestBody @Validated(RegisterVo.add.class) RegisterVo registerVo) {
        SysUser sysUser = new SysUser();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        if (user.getUserType() != 2 && registerVo.getUserType() != 0) {
            sysUser.setUserType(0);
            throw new CampusException("无权操作，只可添加普通用户", 2001);
        }

        BeanUtil.copyProperties(registerVo, sysUser);
        sysUserService.add(sysUser);
        return Result.ok(sysUser);
    }

    @ApiOperation(value = "会员详情")
    @GetMapping("/getUserSet/{id}")
    public Result getUser(@PathVariable Long id) {

        SysUser sysUser = sysUserService.queryUser(id);
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtil.copyProperties(sysUser, sysUserVo);

        return Result.ok(sysUserVo);
    }

    @ApiOperation(value = "会员更新")
    @PostMapping("/updateUserSet")
    public Result updateUserSet(@RequestBody SysUser sysUser) {
        //0为用户,1管理员，2系统管理员
        //不可操作权限>=的
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        SysUser oldUser = sysUserService.getById(sysUser.getUid());
        if (user.getUserType() <= oldUser.getUserType()) {
            throw new CampusException("无权修改", 2001);
        }
        if (user.getUserType() == 1) {
            //管理员不可修改用户的类型
            sysUser.setUserType(oldUser.getUserType());
        }
        if (sysUser.getUserType() == 2) {
            //系统管理员暂定需要自己手动数据库添加
            throw new CampusException("禁止修改成系统管理员", 2001);
        }

        boolean b;
        if (ObjectUtil.isEmpty(sysUser.getPassword())) {
            b = sysUserService.update(sysUser);
        } else {
            //只修改密码
            oldUser.setPassword(new SysUserServiceImpl().BCrypt(sysUser.getPassword()));
            b = sysUserService.updateById(oldUser);
        }

        if (b) {
            return Result.ok(b);
        } else {
            return Result.fail(b);
        }

    }

    @ApiOperation(value = "上传头像")
    @PostMapping("headPortrait")
    public Result uploadHeadPortrait(MultipartFile file, SysUserVo sysUserVo, HttpServletResponse response) {


        String contentType = file.getContentType();
        if (contentType.contains("image")) {
            //上传文件
//        String url = interfaceService.FileAliYunUpload(file);
//            String url = interfaceService.FileLocalUpload(file, userInfo.getUid(), "head/" + userInfo.getUid() + "-" + RandomUtil.randomString(5));

            FileInfoVo fileInfoVo = fileInfoService.uploadHeadImageFile(sysUserVo.getUid(), file);

            return Result.ok(fileInfoVo.getUrl());
        } else {
            response.setStatus(500);
            return Result.fail("只能上传图片");
        }

    }


}
