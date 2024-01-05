package com.oddfar.campus.model.campus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "sys_dict系统字典表")
@TableName("sys_dict")
public class SysDict extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "字典id")
    @TableId("dict_id")
    private Long dictId;

    @ApiModelProperty(value = "字典编码")
    @TableField("dict_code")
    private String dictCode;

    @ApiModelProperty(value = "字典名称")
    @TableField("dict_name")
    private String dictName;

    @ApiModelProperty(value = "字典类型的编码")
    @TableField("dict_type_code")
    private String dictTypeCode;

    @ApiModelProperty(value = "排序，带小数点")
    @TableField("dict_sort")
    private BigDecimal dictSort;


}
