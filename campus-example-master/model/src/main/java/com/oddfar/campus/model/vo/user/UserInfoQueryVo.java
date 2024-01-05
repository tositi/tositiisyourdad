package com.oddfar.campus.model.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="会员搜索对象")
public class UserInfoQueryVo {

    @ApiModelProperty(value = "关键字-用户昵称")
    private String keyword;

    @ApiModelProperty(value = "用户昵称")
    private String userName;

    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "用户邮箱")
    private String userEmail;

//    @ApiModelProperty(value = "手机号")
//    private String phone;

    @ApiModelProperty(value = "状态")
    private Integer status;
    
    @ApiModelProperty(value = "开始时间")
    private String createTimeBegin;

    @ApiModelProperty(value = "结束时间")
    private String createTimeEnd;


}
