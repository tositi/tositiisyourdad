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
@Constraint(validatedBy = UserNameValidator.class)
public @interface UserName {

    String message() default "请填写2-10位 字母/数字/汉字/下划线 的组合";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
