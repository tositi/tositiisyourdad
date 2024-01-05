package com.oddfar.campus.service.api.impl;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.aliyun.dm20151123.models.SingleSendMailRequest;
import com.aliyun.dm20151123.models.SingleSendMailResponse;
import com.aliyun.teaopenapi.models.Config;
import com.oddfar.campus.service.api.MailServiceApi;
import com.oddfar.campus.service.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhiyuan
 */
@Service
public class MailServiceImpl implements MailServiceApi {

    @Autowired
    private SysConfigService configService;

    @Async//异步发送邮件
    @Override
    public void sendQQMail(List<String> tos, String subject, String content, Boolean isHtml) {

        String host = configService.selectConfigByKey("sys.email.smtp.host");
        String port = configService.selectConfigByKey("sys.email.smtp.port");
        String account = configService.selectConfigByKey("sys.email.send.account");
        String password = configService.selectConfigByKey("sys.email.password");


        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(host);
        mailAccount.setPort(Integer.parseInt(port));
        mailAccount.setAuth(true);
        mailAccount.setFrom(account);
        mailAccount.setPass(password);
        //在使用QQ或Gmail邮箱时，需要强制开启SSL支持
        mailAccount.setSslEnable(true);

        MailUtil.send(mailAccount, tos, subject, content, isHtml);
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

    @Override
    public void sendALiMail(String tos, String subject, String content) {
        com.aliyun.dm20151123.Client client = null;

        String name = configService.selectConfigByKey("sys.email.name");
        String accessKeyId = configService.selectConfigByKey("sys.aliyun.mail.accessKeyId");
        String accessKeySecret = configService.selectConfigByKey("sys.aliyun.mail.accessKeySecret");

        try {
            client = this.createClient(accessKeyId, accessKeySecret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SingleSendMailRequest singleSendMailRequest = new SingleSendMailRequest()
                .setAccountName(name)
                .setAddressType(1)
                .setToAddress(tos)
                .setSubject(subject)
                .setTextBody(content)
                .setReplyToAddress(true);
        // 复制代码运行请自行打印 API 的返回值
        SingleSendMailResponse singleSendMailResponse = null;
        try {
            singleSendMailResponse = client.singleSendMail(singleSendMailRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
