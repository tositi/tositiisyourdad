package com.oddfar.campus.model.campus;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "user_role表")
@TableName("user_role")
public class UserRole {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "user_id")
    @TableId("user_id")
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "role_id")
    @TableId("role_id")
    private Long roleId;

}
