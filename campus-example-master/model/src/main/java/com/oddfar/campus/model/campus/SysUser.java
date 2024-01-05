package com.oddfar.campus.model.campus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "sys_user表")
@TableName("sys_user")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "uid")
    @TableId("uid")
    private Long uid;

    @ApiModelProperty(value = "用户账号")
    @TableField("user_account")
    private String userAccount;

    @ApiModelProperty(value = "用户昵称")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "用户邮箱")
    @TableField("user_email")
    private String userEmail;

    /**
     * 因为就三个固定角色，用户，管理员，系统管理员，所以就写死，好区分
     */
    @ApiModelProperty(value = "用户类型（0为用户,1管理员，2系统管理员）")
    @TableField("user_type")
    private Integer userType;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "头像")
    @TableField("avatar")
    private Long avatar;

    @ApiModelProperty(value = "头像")
    @TableField(exist = false)
    private String imageUrl;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;


    @ApiModelProperty(value = "状态（0：锁定 1：正常）")
    @TableField("status")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer status;

}
