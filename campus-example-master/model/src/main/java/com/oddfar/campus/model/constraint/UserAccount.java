package com.oddfar.campus.model.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhiyuan
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
//代表处理逻辑是 UserAccountValidator 类
@Constraint(validatedBy = UserAccountValidator.class)
public @interface UserAccount {

    String message() default "请填写3-10位 字母/数字 组合";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
