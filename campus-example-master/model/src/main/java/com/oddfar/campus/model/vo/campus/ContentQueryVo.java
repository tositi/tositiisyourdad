package com.oddfar.campus.model.vo.campus;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhiyuan
 */
@Data
public class ContentQueryVo {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "分类mid")
    private Long mid;

    @ApiModelProperty(value = "页数")
    private int page;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "cid不能为空", groups = {delete.class})
    @ApiModelProperty(value = "cid")
    private Long cid;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "uid")
    private Long uid;

    @ApiModelProperty(value = "内容")
    private String content;


    /**
     * 删除
     */
    public @interface delete {
    }

}
