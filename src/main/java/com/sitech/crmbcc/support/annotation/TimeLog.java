package com.sitech.crmbcc.support.annotation;

import com.sitech.crmbcc.support.aspect.TimeLogAspect;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 耗时日志注解，配合 {@link TimeLogAspect} 使用，放在方法上
 * <p>注意：
 * <p>1. 如果所有属性都不展示，且包括描述为 ""，将不会打印日志
 * <p>2. 如果方法在执行期间发生异常，将永远不会打印耗时和结束时间
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/10 20:13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeLog {

    @AliasFor("description")
    String value() default "";

    /**
     * 描述
     */
    String description() default "";

    /**
     * 是否展示类的名称
     */
    boolean showClassName() default true;

    /**
     * 是否展示方法的名称
     */
    boolean showMethodName() default true;

    /**
     * 是否展示开始时间
     */
    boolean showStartTime() default true;

    /**
     * 是否展示结束时间
     */
    boolean showEndTime() default true;

    /**
     * 是否展示耗时
     */
    boolean showTotalTime() default true;
}
