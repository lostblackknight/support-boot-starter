package com.sitech.crmbcc.support.annotation;

import com.sitech.crmbcc.support.aspect.RequestLogAspect;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 请求日志注解，配合 {@link RequestLogAspect} 使用，放在方法上或类上
 * <p>注意：
 * <p>1. 一个类上和当前类的方法上都有该注解，采用就近原则，采用方法上的配置。描述的属性会进行组合
 * <p>例如：如果类上的描述为 TestController 方法上的描述为 Hello => 最终描述为 TestController | Hello
 * <p>2. 如果所有属性都不展示，且包括描述为 ""，将不会打印日志
 * <p>3. 如果方法在执行期间发生异常，将永远不会打印响应参数和耗时
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/10 10:37
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLog {

    @AliasFor("description")
    String value() default "";

    /**
     * 描述
     */
    String description() default "";

    /**
     * 是否展示 IP 地址
     */
    boolean showIpAddress() default true;

    /**
     * 是否展示请求的方法的类型
     */
    boolean showMethod() default true;

    /**
     * 是否展示请求的 URI
     */
    boolean showRequestURI() default true;

    /**
     * 是否展示请求参数
     */
    boolean showRequestParam() default true;

    /**
     * 是否展示响应参数
     */
    boolean showResponseParam() default true;

    /**
     * 是否展示请求的类的名称
     */
    boolean showClassName() default true;

    /**
     * 是否展示请求的方法的名称
     */
    boolean showMethodName() default true;

    /**
     * 是否展示请求的耗时
     */
    boolean showTotalTime() default true;
}
