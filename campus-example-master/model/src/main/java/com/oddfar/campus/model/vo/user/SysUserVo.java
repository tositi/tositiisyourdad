package com.oddfar.campus.model.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.constraint.UserAccount;
import com.oddfar.campus.model.constraint.UserName;
import com.oddfar.campus.model.entity.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "SysUserVo")
public class SysUserVo extends BaseRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "uid")
    @NotNull(message = "用户id不能为空", groups = {edit.class, delete.class, detail.class})
    private Long uid;

    @ApiModelProperty(value = "用户账号")
    @UserAccount(groups = {add.class})
    private String userAccount;

    @ApiModelProperty(value = "用户昵称")
    @UserName(groups = {add.class})
    private String userName;

    @ApiModelProperty(value = "用户邮箱")
    @NotNull(message = "邮箱不能为空", groups = {add.class})
    @Pattern(message = "请输入正确邮箱", regexp = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?", groups = {add.class})
    private String userEmail;

    @ApiModelProperty(value = "用户类型（0为用户,1管理员，2系统管理员）")
    private Integer userType;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "头像")
    private Long avatar;

    @ApiModelProperty(value = "头像")
    private String imageUrl;

    @ApiModelProperty(value = "状态（0：锁定 1：正常）")
    private Integer status;

    @ApiModelProperty(value = "IP")
    private String ip;

}
