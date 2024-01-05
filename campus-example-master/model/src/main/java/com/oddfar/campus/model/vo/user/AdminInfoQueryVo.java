package com.oddfar.campus.model.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "会员搜索对象")
public class AdminInfoQueryVo {

    @ApiModelProperty(value = "姓名或昵称")
    private String name;

    @ApiModelProperty(value = "密码")
    private String account;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private String createTimeBegin;

    @ApiModelProperty(value = "结束时间")
    private String createTimeEnd;


}
