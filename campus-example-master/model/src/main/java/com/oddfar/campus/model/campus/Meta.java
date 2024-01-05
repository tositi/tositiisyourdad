package com.oddfar.campus.model.campus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "metas分类表")
@TableName("metas")
public class Meta extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "分类主键")
    @TableId("mid")
    private Long mid;

    @ApiModelProperty(value = "分类名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "缩略名")
    @TableField("slug")
    private String slug;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;


}
