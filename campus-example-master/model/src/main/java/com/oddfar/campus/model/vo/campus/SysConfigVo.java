package com.oddfar.campus.model.vo.campus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "SysConfigVo系统配置vo")
public class SysConfigVo extends BaseRequest {
    /**
     * 参数主键
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "配置id")
    @NotNull(message = "配置id不能为空", groups = {edit.class, delete.class, detail.class})
    private Long configId;

    /**
     * 参数名称
     */
    @ApiModelProperty(value = "参数名称")
    @NotNull(message = "参数名称不能为空", groups = {add.class})
    @Size(min = 0, max = 100, message = "参数名称不能超过100个字符", groups = {add.class, edit.class})
    private String configName;

    /**
     * 参数键名
     */
    @ApiModelProperty(value = "参数键名")
    @NotBlank(message = "参数键名长度不能为空", groups = {add.class})
    @Size(min = 0, max = 100, message = "参数键名长度不能超过100个字符", groups = {add.class, edit.class})
    @TableField("config_key")
    private String configKey;

    /**
     * 参数键值
     */
    @ApiModelProperty(value = "参数键值")
    @NotBlank(message = "参数键值不能为空", groups = {add.class})
    @Size(min = 0, max = 500, message = "参数键值长度不能超过500个字符", groups = {add.class, edit.class})
    private String configValue;

    @ApiModelProperty(value = "系统内置（Y是 N否） ")
    private String configType;

    @NotBlank(message = "所属分类的编码不能为空", groups = {add.class})
    @ApiModelProperty(value = "所属分类的编码")
    private String groupCode;

    @ApiModelProperty(value = "备注")
    private String remark;
}
