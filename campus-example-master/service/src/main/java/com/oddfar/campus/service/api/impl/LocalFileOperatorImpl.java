package com.oddfar.campus.service.api.impl;

import cn.hutool.core.io.FileUtil;
import com.oddfar.campus.model.campus.FileInfo;
import com.oddfar.campus.service.api.FileOperatorApi;
import com.oddfar.campus.service.service.SysConfigService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author zhiyuan
 */
@Service
public class LocalFileOperatorImpl implements FileOperatorApi {


    @Resource
    private SysConfigService configService;

    private static String currentSavePath;
    private static String filePath;
    private static String fileBucket;
    private static String move_bucket;//sys.file.move.default.bucket
    private static String move_path;

    public LocalFileOperatorImpl(SysConfigService configService) {
        this.configService = configService;
        initClient();
    }

    //初始化
    public void initClient() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (isWindows) {
            filePath = configService.selectConfigByKey("sys.local.file.save.path.windows");
        } else {
            filePath = configService.selectConfigByKey("sys.local.file.save.path.linux");
        }

        fileBucket = configService.selectConfigByKey("sys.file.default.bucket");
        String time = new DateTime().toString("yyyy-MM");
        //路径 = 文件路径 + 桶Bucket + 年-月
        currentSavePath = filePath + File.separator + fileBucket + File.separator + time;
        if (!FileUtil.exist(currentSavePath)) {
            FileUtil.mkdir(currentSavePath);
        }
        //移动的目录
        move_bucket = configService.selectConfigByKey("sys.file.move.default.bucket");
        move_path = filePath + File.separator + move_bucket + File.separator + time;
        if (!FileUtil.exist(move_path)) {
            FileUtil.mkdir(move_path);
        }
    }

    @Override
    public void storageFile(FileInfo fileInfo, byte[] bytes) {
        fileInfo.setPath(currentSavePath);
        String absoluteFile = currentSavePath + File.separator + fileInfo.getObjectName();
        FileUtil.writeBytes(bytes, absoluteFile);

    }

    @Override
    public void deleteFile(FileInfo fileInfo) {

        String savaPath = fileInfo.getPath() + File.separator + fileInfo.getObjectName();
        FileUtil.del(savaPath);
    }

    @Override
    public void moveFile(FileInfo fileInfo) {
        String savaPath = fileInfo.getPath() + File.separator + fileInfo.getObjectName();
        fileInfo.setPath(move_path);
        fileInfo.setState(2);
        fileInfo.setBucket(move_bucket);
        FileUtil.move(FileUtil.file(savaPath), FileUtil.file(move_path), true);
    }
}
