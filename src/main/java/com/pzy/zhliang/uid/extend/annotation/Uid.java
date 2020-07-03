package com.pzy.zhliang.uid.extend.annotation;

import java.lang.annotation.*;

/**
 * @作者  庄梦蝶殇
 * @创建时间 2018年8月31日 下午4:18:51
 * @版本 1.00
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Uid {
    /**
     * uid 生成模式
     */
    UidModel model() default UidModel.step;

    /**
     * 前缀
     */
    String prefix() default "";

    /**
     * 是否数字类型
     */
    boolean isNum() default true;
}
