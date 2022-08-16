package com.sitech.crmbcc.support.annotation.security;

import com.sitech.crmbcc.support.enums.security.DesensitizedType;
import com.sitech.crmbcc.support.serializer.DesensitizedAnnotationSensitiveSerializer;

import java.lang.annotation.*;

/**
 * 默认的脱敏注解，放在属性上。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see DesensitizedType
 * @see DesensitizedAnnotationSensitiveSerializer
 * @since 2022/8/13 17:23
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Desensitized {

    /**
     * 支持脱敏的类型
     */
    DesensitizedType value();
}
