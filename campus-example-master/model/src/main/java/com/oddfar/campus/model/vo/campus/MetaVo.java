package com.oddfar.campus.model.vo.campus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "MetaVo分类对象")
public class MetaVo extends BaseRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "分类主键")
    @NotNull(message = "分类id不能为空", groups = {edit.class, delete.class, detail.class})
    private Long mid;

    @NotNull(message = "分类名不能为空", groups = {add.class})
    @ApiModelProperty(value = "分类名")
    private String name;

    @NotNull(message = "缩略名不能为空", groups = {add.class})
    @ApiModelProperty(value = "缩略名")
    private String slug;

    @ApiModelProperty(value = "描述")
    private String description;
}
