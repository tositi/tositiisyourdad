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
@TableName("comments")
public class Comment extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "评论主键")
    @TableId("coid")
    private Long coid;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "上级id")
    @TableField("parent_id")
    private Long parentId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "用户id")
    @TableField("uid")
    private Long uid;


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "内容id")
    @TableField("cid")
    private Long cid;

    @ApiModelProperty(value = "评论内容")
    @TableField("co_content")
    private String coContent;

    @ApiModelProperty(value = "是否包含子节点")
    @TableField(exist = false)
    private boolean hasChildren;

    @ApiModelProperty(value = "子节点")
    @TableField(exist = false)
    private List<Comment> children;

}
