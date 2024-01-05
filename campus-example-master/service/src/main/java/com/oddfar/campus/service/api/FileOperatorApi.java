package com.oddfar.campus.service.api;

import com.oddfar.campus.model.campus.FileInfo;

/**
 * @author zhiyuan
 */
public interface FileOperatorApi {

    /**
     *  储存文件
     */
    void storageFile(FileInfo fileInfo, byte[] bytes);

    /**
     * 删除文件
     */
    void deleteFile(FileInfo fileInfo);

    /**
     * 移动文件
     */
    void moveFile(FileInfo fileInfo);
}
