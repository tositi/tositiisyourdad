package com.oddfar.campus.model.vo.campus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "CommentVo评论对象")
public class CommentVo extends BaseRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "被评论的主键")
    @NotNull(message = "coid不能为空", groups = {delete.class})
    private Long coid;


//    @JsonFormat(shape = JsonFormat.Shape.STRING)
//    @ApiModelProperty(value = "用户id")
//    private Long uid;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "内容id")
    @NotNull(message = "cid不能为空", groups = {add.class})
    private Long cid;

    @ApiModelProperty(value = "评论内容")
    @NotNull(message = "评论内容不能为空", groups = {add.class})
    @Size(message = "最多200字符", max = 200, groups = {add.class})
    private String coContent;

}
