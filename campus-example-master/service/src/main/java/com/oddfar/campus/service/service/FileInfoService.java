package com.oddfar.campus.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.model.campus.FileInfo;
import com.oddfar.campus.model.vo.campus.FileInfoVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author zhiyuan
 */
public interface FileInfoService extends IService<FileInfo> {


    /**
     * 用户上传头像
     */
    FileInfoVo uploadHeadImageFile(Long uid, MultipartFile file);

    /**
     * 上传用户发表信息墙的文件
     */
    FileInfoVo uploadContentFile(Long uid, Long cid, MultipartFile file);

    /**
     * 当发表的信息墙被删除时，对应文件的处理
     * @param idList 文件ID列表
     */
    void delContentFile(List<Long> idList);

    /**
     * 获取文件的URL
     */
    String getFileInfoUrl(Long fileId);
}
