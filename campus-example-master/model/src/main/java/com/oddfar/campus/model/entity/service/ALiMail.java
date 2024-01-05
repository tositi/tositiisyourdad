package com.oddfar.campus.model.entity.service;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhiyuan
 */
@Data
@AllArgsConstructor
public class ALiMail {
    /**
     * 发送的邮件地址
     */
    private String toAddress;

    /**
     * 发送的主题
     */
    private String subject;

    /**
     * 发送的内容
     */
    private String textBody;
}
