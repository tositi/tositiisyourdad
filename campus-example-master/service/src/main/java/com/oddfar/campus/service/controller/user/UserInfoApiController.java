package com.oddfar.campus.service.controller.user;


import cn.hutool.core.bean.BeanUtil;
import com.oddfar.campus.common.constant.Constants;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.common.utils.IpUtils;
import com.oddfar.campus.common.utils.VerificationCode;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.model.vo.user.RegisterVo;
import com.oddfar.campus.model.vo.user.SysUserVo;
import com.oddfar.campus.service.service.FileInfoService;
import com.oddfar.campus.service.service.SysUserService;
import com.oddfar.campus.service.service.impl.SysUserServiceImpl;
import com.oddfar.campus.service.utils.SecurityUtils;
import com.oddfar.campus.service.utils.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @author zhiyuan
 */
@RestController
@RequestMapping("/api/userInfo")
@Api(tags = "用户信息接口")
@Slf4j
//@CrossOrigin
public class UserInfoApiController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private TokenService tokenService;


    @ApiOperation(value = "会员注册")
    @PostMapping("/register")
    public Result register(@RequestBody @Validated({RegisterVo.add.class, RegisterVo.register.class}) RegisterVo registerVo,
                           HttpServletRequest request) {
        String code = redisTemplate.opsForValue().get(Constants.CAPTCHA_CODE_KEY + registerVo.getUserEmail());
        if (StringUtils.isEmpty(code)) {
            throw new CampusException("请获取验证码", 2001);
        }
        if (!code.equals(registerVo.getCode())) {
            throw new CampusException("邮箱验证码不对", 2001);
        }

        registerVo.setIp(IpUtils.getIpAddr(request));
        Map<String, Object> register = sysUserService.register(registerVo);

        return Result.ok(register);
    }

    @ApiOperation(value = "会员修改密码")
    @PostMapping("/edit")
    public Result edit(@RequestBody @Validated(RegisterVo.edit.class) RegisterVo registerVo) {
        String code = redisTemplate.opsForValue().get("Retrieve-" + registerVo.getUserEmail());
        if (StringUtils.isEmpty(code)) {
            throw new CampusException("请获取验证码", 2001);
        }
        if (!code.equals(registerVo.getCode())) {
            throw new CampusException("邮箱验证码不对", 2001);
        }

        //获取账号并设置新密码
        SysUser sysUser = sysUserService.getByAccount(registerVo.getUserEmail());
        sysUser.setPassword(new SysUserServiceImpl().BCrypt(registerVo.getPassword()));

        sysUserService.updateById(sysUser);
        //删除key
        redisTemplate.delete("Retrieve-" + registerVo.getUserEmail());

        return Result.ok("修改成功");
    }

    @ApiOperation(value = "会员修改昵称")
    @PostMapping("/editName")
    public Result editName(@RequestBody @Validated(RegisterVo.name.class) RegisterVo registerVo) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        SysUser loginUserInfo = sysUserService.getLoginUserInfo();
        loginUserInfo.setUserName(registerVo.getUserName());
        //把数据库查询的放入redis
        BeanUtil.copyProperties(loginUserInfo, user);
        if (sysUserService.update(user)) {
            // 更新缓存用户信息
            tokenService.setLoginUser(loginUser);
        }

        return Result.ok("修改成功");
    }


    @ApiOperation(value = "会员信息")
    @PostMapping("/info")
    public Result info() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
//        SysUser user = sysUserService.getLoginUserInfo();
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtil.copyProperties(user, sysUserVo);
        return Result.ok(sysUserVo);
    }

    @ApiOperation(value = "验证码")
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {

        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("verify_code", text);
        VerificationCode.output(image, resp.getOutputStream());

    }


}
