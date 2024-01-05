package com.oddfar.campus.common.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    PARAM_ERROR(202, "参数不正确"),
    SERVICE_ERROR(203, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    DATA_UPDATE_ERROR(205, "数据版本异常"),

    LOGIN_ACCOUNT_OR_PASSWORD_ERROR(211, "账号或密码不正确"),
    LOGIN_DISABLED_ERROR(212, "该用户已被禁用"),

    ACCOUNT_EXIST(216, "账号或昵称已存在"),

    ;

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}