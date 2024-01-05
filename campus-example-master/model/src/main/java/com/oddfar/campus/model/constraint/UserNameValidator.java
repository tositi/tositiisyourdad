package com.oddfar.campus.model.constraint;

import cn.hutool.core.util.ObjectUtil;
import com.oddfar.campus.common.utils.NameJudgeUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author zhiyuan
 */
public class UserNameValidator implements ConstraintValidator<UserName, Object> {


    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (ObjectUtil.isEmpty(o)) {
            return false;
        }
        boolean numeric = NameJudgeUtil.isName(o.toString());
        if (!numeric) {
            return false;
        }
        return true;
    }
}
