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
@ApiModel(description = "pictures图片文件表")
@TableName("pictures")
public class Pictures extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "图片主键")
    @TableId("pid")
    private Long pid;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "用户id")
    @TableField("uid")
    private Long uid;


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "发表内容cid")
    @TableField("cid")
    private Long cid;

    @ApiModelProperty(value = "file_id")
    @TableField("file_id")
    private Long fileId;

}
