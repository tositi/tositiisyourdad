package com.oddfar.campus.service.controller.user;

import cn.hutool.core.collection.CollUtil;
import com.oddfar.campus.common.constant.Constants;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.entity.service.ALiMail;
import com.oddfar.campus.model.vo.user.SysUserVo;
import com.oddfar.campus.service.api.InterfaceService;
import com.oddfar.campus.service.api.MailServiceApi;
import com.oddfar.campus.service.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhiyuan
 */
@RestController
@RequestMapping("/api/service")
@Api(tags = "对接服务的API接口")
public class InterfaceApiController {

    @Autowired
    private InterfaceService interfaceService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private MailServiceApi mailServiceApi;

    //发送手机验证码
    @ApiOperation(value = "发送手机验证码")
    @GetMapping("/sendMsm/{phone}")
    public Result sendCode(@PathVariable String phone) {
        //从redis获取验证码，如果获取获取到，返回ok
        // key 手机号  value 验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return Result.ok("发送成功");
        }
        //如果从redis获取不到，生成六位随机数字验证码
        Random random = new Random();
        DecimalFormat sixdf = new DecimalFormat("000000");
        code = sixdf.format(random.nextInt(1000000));
        //调用service方法，通过整合短信服务进行发送
        boolean isSend = interfaceService.sendPhoneCode(phone, code);
        //生成验证码放到redis里面，设置有效时间
        if (isSend) {
            // 电话、验证码、时间、单位
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return Result.ok("发送成功");
        } else {
            return Result.fail().message("发送短信失败");
        }
    }


    @ApiOperation(value = "注册时发送邮件验证码")
    @GetMapping("/sendMailCode/{mail}")
    public Result sendMail(@PathVariable String mail) {
        if (!isEmail(mail)) {
            return Result.fail().message("请输入正确格式的邮箱");
        }
        //redis邮箱存在则不在发送
        if (redisTemplate.hasKey(mail)) {
            return Result.ok("发送成功");
        }
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setUserEmail(mail);

        //如果邮箱存在
        if (sysUserService.isExist(sysUserVo)) {
            return Result.fail().message("邮箱已被注册");
        }
        Random random = new Random();
        DecimalFormat sixdf = new DecimalFormat("000000");
        String code = sixdf.format(random.nextInt(1000000));


        try {
            // 存入到redis 邮箱、验证码、时间、单位
            redisTemplate.opsForValue().set(Constants.CAPTCHA_CODE_KEY + mail, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
            // 发送邮件
            mailServiceApi.sendQQMail(CollUtil.newArrayList(mail), "注册验证码", "您的验证码是：" + code + "，" + Constants.CAPTCHA_EXPIRATION + "分钟有效", false);
        } catch (Exception e) {
            return Result.fail("失败").message("发送邮件失败");
        }

        return Result.ok("正在发送邮件，请注意查收");
    }

    @ApiOperation(value = "找回密码发送邮件验证码")
    @GetMapping("/sendRetrieveMail/{mail}")
    public Result sendRetrieveMail(@PathVariable String mail) {
        mail = mail.trim();
        if (!isEmail(mail)) {
            return Result.fail().message("请输入正确格式的邮箱");
        }
        //redis邮箱存在则不在发送
        if (redisTemplate.hasKey("Retrieve-" + mail)) {
            return Result.ok("发送成功");
        }
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setUserEmail(mail);

        //如果邮箱不存在
        if (!sysUserService.isExist(sysUserVo)) {
            return Result.fail().message("账号不存在");
        }
        Random random = new Random();
        DecimalFormat sixdf = new DecimalFormat("000000");
        String code = sixdf.format(random.nextInt(1000000));

        ALiMail aLiMail = new ALiMail(mail, "找回密码验证码", "您的验证码是：" + code + "，30分钟有效");

        // 存入到redis 邮箱、验证码、时间、单位
        try {
            redisTemplate.opsForValue().set("Retrieve-" + mail, code, 30, TimeUnit.MINUTES);
            mailServiceApi.sendQQMail(CollUtil.newArrayList(mail), "找回密码验证码", "您的验证码是：" + code + "，30分钟有效", false);
        } catch (Exception e) {
            return Result.fail("失败").message("发送邮件失败");
        }

        return Result.ok("正在发送邮件，请注意查收");
    }


    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }


}
