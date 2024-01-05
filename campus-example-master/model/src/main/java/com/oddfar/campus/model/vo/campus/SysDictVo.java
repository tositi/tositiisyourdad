package com.oddfar.campus.model.vo.campus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "SysDictVo系统字典")
public class SysDictVo extends BaseRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "字典id")
    @NotNull(message = "dictId不能为空", groups = {edit.class, delete.class, detail.class})
    private Long dictId;

    @ApiModelProperty(value = "字典编码")
    @NotNull(message = "字典编码不能为空", groups = {add.class})
    private String dictCode;

    @ApiModelProperty(value = "字典名称")
    @NotNull(message = "字典名称不能为空", groups = {add.class})
    private String dictName;

    @ApiModelProperty(value = "字典类型的编码")
    @NotNull(message = "字典类型的编码不能为空", groups = {add.class})
    private String dictTypeCode;

    @ApiModelProperty(value = "排序，带小数点")
    private BigDecimal dictSort;
}
