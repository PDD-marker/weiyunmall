package com.taotao.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @version 1.0
 * @quther 孤郁
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {

    Set<Integer> set = new HashSet<>();

    //初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for (int temp:vals) {
            set.add(temp);
        }
    }

    //检验器

    /**
     *
     * @param integer 需要校验的数值
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(integer);
    }
}
