package com.oddfar.campus.model.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhiyuan
 */
@Data
public class BaseRequest implements Serializable {


    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTimeBegin;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTimeEnd;


    @ApiModelProperty(value = "其他参数")
    private Map<String, Object> otherParams;

    /**
     * 查
     */
    public @interface detail {
    }

    /**
     * 删
     */
    public @interface delete {
    }

    /**
     * 更
     */
    public @interface edit {
    }

    /**
     * 增
     */
    public @interface add {
    }

    public @interface list {
    }

    public @interface page {
    }
}
