package com.oddfar.campus.service.api.impl;


import cn.hutool.core.util.ObjectUtil;
import com.aliyun.dm20151123.models.SingleSendMailRequest;
import com.aliyun.dm20151123.models.SingleSendMailResponse;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.teaopenapi.models.Config;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.model.entity.service.ALiMail;
import com.oddfar.campus.service.api.InterfaceService;
import com.oddfar.campus.service.service.SysUserService;
import com.oddfar.campus.service.utils.ConstantOssPropertiesUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class InterfaceServiceImpl implements InterfaceService {

//    @Value("${file.address}")
//    private String address;
//    @Value("${file.hostname}")
//    private String hostname;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 模拟短信验证
     *
     * @param phone
     * @param code
     * @return
     */
    @Override
    public boolean sendPhoneCode(String phone, String code) {
        //判断手机号是否为空
        if (ObjectUtil.isEmpty(phone)) {
            throw new CampusException("手机号长度不对", 2001);
        }
        if (phone.length() != 11) {
            throw new CampusException("手机号长度不对", 2001);
        }


        log.info("验证码：" + code);
//        if (tengSend(phone, code)) {
//            return true;
//        } else {
//            return false;
//        }

        return true;
    }

    /**
     * 阿里云邮件服务
     *
     * @return
     */
    @Override
    public boolean sendMail(ALiMail aLiMail) {
        com.aliyun.dm20151123.Client client = null;
        try {
            //根据自己的设置，没有就开通，否则邮件服务用不了
            client = this.createClient("XXXXXXXXXX", "XXXXXXXXXXX");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SingleSendMailRequest singleSendMailRequest = new SingleSendMailRequest()
                .setAccountName("admin@oddfar.com")
                .setAddressType(1)
                .setToAddress(aLiMail.getToAddress())
                .setSubject(aLiMail.getSubject())
                .setTextBody(aLiMail.getTextBody())
                .setReplyToAddress(true);
        // 复制代码运行请自行打印 API 的返回值
        SingleSendMailResponse singleSendMailResponse = null;
        try {
            singleSendMailResponse = client.singleSendMail(singleSendMailRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    @Override
    public String FileAliYunUpload(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantOssPropertiesUtils.EDNPOINT;
        String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantOssPropertiesUtils.SECRECT;
        String bucketName = ConstantOssPropertiesUtils.BUCKET;
        try {
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            //生成随机唯一值，使用uuid，添加到文件名称里面
            String uuid = UUID.randomUUID().toString();
//            fileName = uuid + fileName;
            fileName = uuid + "." + StringUtils.substringAfterLast(fileName, ".");
            //按照当前日期，创建文件夹，上传到创建文件夹里面
            //  2021/02/02/02.jpg
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            fileName = timeUrl + "/" + fileName;
            //调用方法实现上传
            ossClient.putObject(bucketName, fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //上传之后文件路径
            // https://campus-oddfar.oss-cn-beijing.aliyuncs.com/01.jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            //返回
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dm20151123.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dm.aliyuncs.com";
        return new com.aliyun.dm20151123.Client(config);
    }


    /**
     * 腾讯云
     *
     * @param phone
     * @param code
     * @return
     */
    public boolean tengSend(String phone, String code) {
        //判断手机号是否为空
        if (ObjectUtil.isEmpty(phone)) {
            return false;
        }

        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential("XXXXXXXXXXX", "XXXXXXXXXXXXXXXXXX");
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            phone = "+86" + phone;
            String[] phoneNumberSet1 = {phone};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setSmsSdkAppId("1400564343");
            req.setSignName("赵深宸文章分享");
            req.setTemplateId("1091168");

            String[] templateParamSet1 = {code, "5"};
            req.setTemplateParamSet(templateParamSet1);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        log.info("验证码：" + code);

        return true;

    }


}
