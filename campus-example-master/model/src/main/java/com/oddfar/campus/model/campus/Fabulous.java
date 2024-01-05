package com.oddfar.campus.model.campus;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "fabulous点赞表")
@TableName("fabulous")
public class Fabulous  {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "点赞主键")
    @TableId("fid")
    private Long fid;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "用户id")
    @TableField("uid")
    private Long uid;


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "发表内容cid")
    @TableField("cid")
    private Long cid;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time")
    private Date updateTime;

}
