package com.oddfar.campus.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.model.campus.FileInfo;
import com.oddfar.campus.model.campus.Pictures;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.vo.campus.FileInfoVo;
import com.oddfar.campus.service.api.FileOperatorApi;
import com.oddfar.campus.service.mapper.FileInfoMapper;
import com.oddfar.campus.service.service.*;
import com.oddfar.campus.service.utils.FileInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author zhiyuan
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysConfigService configService;
    @Autowired
    private FileOperatorApi fileOperatorApi;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private ContentService contentService;


    public FileInfo uploadFile(MultipartFile file) {
        //获取文件信息
        FileInfo fileInfo = FileInfoFactory.createFileInfo(file);

        String fileBucket = configService.selectConfigByKey("sys.file.default.bucket");
        fileInfo.setBucket(fileBucket);
        /**
         * 设置为1-本地，2-网络，3-阿里云
         * 但文件储存方式目前只有本地，没想好怎么设计，待补充。
         */
        //TODO 文件储存方式
        fileInfo.setLocation(1);

        try {
            //保存文件到本地和存入数据库信息
            fileOperatorApi.storageFile(fileInfo, file.getBytes());
            this.save(fileInfo);
        } catch (IOException e) {
            this.removeById(fileInfo);
            e.printStackTrace();
        }

        return fileInfo;

    }

    @Override
    public FileInfoVo uploadHeadImageFile(Long uid, MultipartFile file) {
        this.uidIsExit(uid);

        SysUser user = sysUserService.queryUser(uid);
        //删除原头像 根据 fileID
        if (ObjectUtil.isNotEmpty(user.getAvatar())) {
            fileOperatorApi.deleteFile(baseMapper.selectById(user.getAvatar()));
        }
        //上传文件
        FileInfo fileInfo = this.uploadFile(file);
        user.setAvatar(fileInfo.getFileId());
        //修改数据库
        sysUserService.update(user);

        FileInfoVo fileInfoVo = new FileInfoVo();
        BeanUtil.copyProperties(fileInfo, fileInfoVo);
        fileInfoVo.setUrl(this.getFileInfoUrl(fileInfo.getFileId()));
        return fileInfoVo;

    }

    @Override
    public FileInfoVo uploadContentFile(Long uid, Long cid, MultipartFile file) {
        this.uidIsExit(uid, cid);
        //上传文件
        FileInfo fileInfo = this.uploadFile(file);
        //修改添加数据库

        FileInfoVo fileInfoVo = new FileInfoVo();
        BeanUtil.copyProperties(fileInfo, fileInfoVo);
        fileInfoVo.setUrl(this.getFileInfoUrl(fileInfo.getFileId()));

        Pictures picture = new Pictures();
        picture.setCid(cid);
        picture.setUid(uid);
        picture.setFileId(fileInfoVo.getFileId());
        pictureService.add(picture);

        return fileInfoVo;
    }

    @Override
    public void delContentFile(List<Long> idList) {
        Boolean isDel = Boolean.valueOf(configService.selectConfigByKey("sys.file.is.delete"));
        if (isDel) {
            //删除
            for (Long id : idList) {
                fileOperatorApi.deleteFile(this.getById(id));
            }
            this.removeByIds(idList);
        } else {
            //移动
            for (Long id : idList) {
                //文件操作
                FileInfo fileInfo = this.getById(id);
                fileOperatorApi.moveFile(fileInfo);
                //数据库操作
                this.updateById(fileInfo);
            }
        }
    }

    @Override
    public String getFileInfoUrl(Long fileId) {
        FileInfo fileInfo = this.getById(fileId);
        if (fileInfo.getLocation() == 2) {
            //如果是网络图片
            return fileInfo.getPath();
        }
        String host = configService.selectConfigByKey("sys.server.deploy.host");
        Boolean nginx = Boolean.valueOf(configService.selectConfigByKey("sys.file.visit.nginx"));
        //如果是通过nginx映射
        if (nginx) {
            String fileSavePath = fileInfo.getPath() + fileInfo.getBucket() + fileInfo.getObjectName();
            fileSavePath = fileSavePath.replaceAll("\\\\", "/");
            //host + 文件路径
            return host + "/" + fileSavePath;
        } else {
            return host + "/filePreview/" + fileId;
        }


    }


    public void uidIsExit(Long uid) {
        if (ObjectUtil.isEmpty(sysUserService.getById(uid))) {
            throw new CampusException("uid不存在", 2001);
        }
    }

    public void uidIsExit(Long uid, Long cid) {
        if (ObjectUtil.isEmpty(sysUserService.getById(uid))) {
            throw new CampusException("uid不存在", 2001);
        }
        if (ObjectUtil.isEmpty(contentService.getById(cid))) {
            throw new CampusException("cid不存在", 2001);
        }
    }
}
