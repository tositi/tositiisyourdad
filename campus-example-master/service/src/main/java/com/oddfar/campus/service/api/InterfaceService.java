package com.oddfar.campus.service.api;

import com.oddfar.campus.model.entity.service.ALiMail;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhiyuan
 */
public interface InterfaceService  {
    //发送手机验证码
    boolean sendPhoneCode(String phone, String code);

    //发送邮件
    boolean sendMail(ALiMail aLiMail);

    //上传文件到阿里云oss
    String FileAliYunUpload(MultipartFile file);


}
