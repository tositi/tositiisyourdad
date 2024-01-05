package com.oddfar.campus.model.campus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "contents内容表")
@TableName("contents")
//@EqualsAndHashCode(callSuper = true)
public class Content extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "分类主键")
    @TableId("cid")
    private Long cid;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "用户id")
    @TableField("uid")
    private Long uid;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "分类id")
    @TableField("mid")
    private Long mid;

    @ApiModelProperty(value = "内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "状态，0审核，1正常，2下架")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "文章：0文字，1图片，2视频")
    @TableField("ctype")
    private Integer ctype;

    @ApiModelProperty(value = "图片数量")
    @TableField("picture_num")
    private Integer pictureNum;


    @ApiModelProperty(value = "类型：0：不匿名，1匿名")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "图片列表")
    @TableField(exist = false)
    List<String> pictureUrl;
}
