package com.oddfar.campus.model.vo.user;

import com.oddfar.campus.model.constraint.UserAccount;
import com.oddfar.campus.model.constraint.UserName;
import com.oddfar.campus.model.entity.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "RegisterVo")
public class RegisterVo extends BaseRequest{

    @ApiModelProperty(value = "用户账号")
    @UserAccount(groups = {add.class})
    private String userAccount;

    @ApiModelProperty(value = "用户昵称")
    @UserName(groups = {add.class})
    private String userName;

    @ApiModelProperty(value = "用户邮箱")
    @NotNull(message = "邮箱不能为空", groups = {add.class, edit.class})
    private String userEmail;

    @ApiModelProperty(value = "用户类型")
    private Integer userType;


    @ApiModelProperty(value = "密码")
    @Size(message = "请输入6-10位密码", groups = {add.class, edit.class}, max = 20, min = 6)
    private String password;

    @Size(message = "请输入6位验证码", groups = {register.class}, max = 6, min = 6)
    @ApiModelProperty(value = "验证码")
    private String code;

    @ApiModelProperty(value = "IP")
    private String ip;

    public @interface name {
    }

    public @interface register {
    }


}
