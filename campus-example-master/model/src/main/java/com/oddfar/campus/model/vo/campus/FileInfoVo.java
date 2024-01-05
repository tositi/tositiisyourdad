package com.oddfar.campus.model.vo.campus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "FileInfoVo文件信息")
public class FileInfoVo extends BaseRequest {

    /**
     * 主键id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "主键id")
    private Long fileId;


    /**
     * 文件存储位置：1-本地，2-阿里云
     */
    private Integer location;

    /**
     * 文件仓库（文件夹）
     */
    private String bucket;

    /**
     * 文件名称（上传时候的文件全名）
     */
    private String originName;

    /**
     * 文件后缀，例如.txt
     */
    private String suffix;

    /**
     * 文件大小kb为单位
     */
    private Long sizeKb;


    /**
     * 存储到bucket中的名称，主键id+.后缀
     */
    private String ObjectName;

    /**
     * 存储路径
     */
    private String path;


    /**
     * 当前状态：0-正常，1-禁止，2-移除
     */
    private Integer status;

    /**
     * 文件的访问URL
     */
    private String url;
}
