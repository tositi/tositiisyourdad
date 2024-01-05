package com.oddfar.campus.service.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.oddfar.campus.model.campus.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * 文件信息工具类,根据 Guns 代码修改
 * @author zhiyuan
 */
public class FileInfoFactory {


    public static FileInfo createFileInfo(MultipartFile file) {
        // 生成文件的唯一id
        Long fileId = IdWorker.getId();

        // 获取文件原始名称
        String originalFilename = file.getOriginalFilename();

        // 获取文件后缀（不包含点）
        String fileSuffix = null;
        if (ObjectUtil.isNotEmpty(originalFilename)) {
            fileSuffix = StrUtil.subAfter(originalFilename, ".", true);
        }

        // 生成文件的最终名称
        String finalFileName = fileId + "." + fileSuffix;

        // 计算文件大小kb
        long fileSizeKb = Convert.toLong(NumberUtil.div(new BigDecimal(file.getSize()), BigDecimal.valueOf(1024)).setScale(0, BigDecimal.ROUND_HALF_UP));

        // 封装存储文件信息（上传替换公共信息）
        FileInfo sysFileInfo = new FileInfo();
        sysFileInfo.setFileId(fileId);
        /**
         * 这俩没在此处设置
         */
//        sysFileInfo.setLocation();
//        sysFileInfo.setBucket();
        sysFileInfo.setObjectName(finalFileName);
        sysFileInfo.setOriginName(originalFilename);
        sysFileInfo.setSuffix(fileSuffix);
        sysFileInfo.setSizeKb(fileSizeKb);
        sysFileInfo.setState(0);

        // 返回结果
        return sysFileInfo;
    }
}
