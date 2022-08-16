package com.sitech.crmbcc.support.annotation.security;

import com.sitech.crmbcc.support.aspect.security.SensitiveAspect;
import com.sitech.crmbcc.support.handler.security.SensitiveHandler;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 敏感信息注解，配合 {@link SensitiveAspect} 使用，放在方法上，表明该方法的返回值要进行脱敏处理。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see SensitiveAspect
 * @see SensitiveHandler
 * @since 2022/8/13 15:10
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {

    /**
     * 标记，如果返回值的类型相同，但又要进行不同的处理，这时可以通过 tag 来区分
     */
    @AliasFor("tag")
    String value() default "";

    /**
     * 标记，如果返回值的类型相同，但又要进行不同的处理，这时可以通过 tag 来区分
     */
    String tag() default "";
}
