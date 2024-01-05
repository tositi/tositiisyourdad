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
@ApiModel(description = "role表")
@TableName("role")
public class Role  extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "role_id")
    @TableId("role_id")
    private Long roleId;

    @ApiModelProperty(value = "角色名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "角色权限字符串")
    @TableField("key")
    private String key;

}
