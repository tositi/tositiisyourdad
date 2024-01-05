package com.oddfar.campus.model.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "userInfoVo")
public class UserInfoRequest extends BaseRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "uid")
    @NotNull(message = "分类id不能为空", groups = {edit.class, delete.class, detail.class})
    private Long uid;

    @ApiModelProperty(value = "密码")
    @Size(message = "请输入6-10位密码", groups = {add.class}, max = 20, min = 6)
    @NotNull(message = "密码不能为空", groups = {add.class})
    private String password;

    @NotNull(message = "昵称不能为空", groups = {add.class})
    @ApiModelProperty(value = "姓名或昵称")
    private String userName;

    @NotNull(message = "邮箱不能为空", groups = {add.class})
    @Pattern(message = "请输入正确的QQ邮箱", regexp = "[1-9]\\d{5,10}@qq\\.com", groups = {add.class})
    @Size(message = "请输入6-11位QQ", groups = {add.class}, max = 18, min = 13)
    @ApiModelProperty(value = "邮箱")
    private String userEmail;

//    @ApiModelProperty(value = "手机号")
//    @Size(message = "请输入11位手机号", groups = {add.class}, max = 11, min = 11)
//    @Pattern(message = "请输入正确的手机号", regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$", groups = {add.class})
//    private String userPhone;

    @ApiModelProperty(value = "状态（0：锁定 1：正常）")
    private Integer status;


}
