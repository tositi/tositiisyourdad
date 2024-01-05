package com.oddfar.campus.model.campus;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.oddfar.campus.model.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhiyuan
 */
@Data
@ApiModel(description = "sys_config参数配置表")
@TableName("file_info")
public class FileInfo extends BaseEntity {

    /**
     * 主键id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "主键id")
    @TableId(value = "file_id", type = IdType.ASSIGN_ID)
    private Long fileId;


    /**
     * 文件存储位置：1-本地，2-阿里云
     */
    @TableField("location")
    private Integer location;

    /**
     * 文件仓库（文件夹）
     */
    @TableField("bucket")
    private String bucket;

    /**
     * 文件名称（上传时候的文件全名）
     */
    @TableField("origin_name")
    private String originName;

    /**
     * 文件后缀，例如.txt
     */
    @TableField("suffix")
    private String suffix;

    /**
     * 文件大小kb为单位
     */
    @TableField("size_kb")
    private Long sizeKb;


    /**
     * 存储到bucket中的名称，主键id+.后缀
     */
    @TableField("object_name")
    private String ObjectName;

    /**
     * 存储路径
     */
    @TableField("path")
    private String path;


    /**
     * 当前状态：0-正常，1-禁止，2-移除
     */
    @TableField("state")
    private Integer state;
}
