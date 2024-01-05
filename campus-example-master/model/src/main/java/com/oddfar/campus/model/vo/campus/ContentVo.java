package com.oddfar.campus.model.vo.campus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "发表内容对象")
public class ContentVo extends BaseRequest {

    public @interface upload {
    }


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "分类主键")
    @NotNull(message = "发表内容cid不能为空", groups = {upload.class, edit.class, delete.class, detail.class})
    private Long cid;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "用户id")
    private Long uid;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "分类id不能为空", groups = {add.class, userAdd.class})
    @ApiModelProperty(value = "分类id")
    private Long mid;

    @NotNull(message = "内容不能为空", groups = {add.class, userAdd.class})
    @Size(message = "最多700字符", max = 700, groups = {add.class, userAdd.class})
    @ApiModelProperty(value = "内容")
    private String content;

    @NotNull(message = "状态不能为空", groups = {add.class})
    @Range(min = 0, max = 2)
    @ApiModelProperty(value = "状态，0审核，1正常，2下架")
    private Integer status;

    @NotNull(message = "文章内容类型不能为空", groups = {add.class, userAdd.class})
    @Range(min = 0, max = 2)
    @ApiModelProperty(value = "文章内容类型：0文字，1图片,2视频")
    private Integer ctype;

    @NotNull(message = "类型不能为空", groups = {add.class, userAdd.class})
    @Range(min = 0, max = 1)
    @ApiModelProperty(value = "类型：0：不匿名，1匿名")
    private Integer type;

    @ApiModelProperty(value = "图片数量")
    @NotNull(message = "pictureNum不能为空", groups = {upload.class})
    private Integer pictureNum;


    /**
     * 增
     */
    public @interface userAdd {
    }


}
