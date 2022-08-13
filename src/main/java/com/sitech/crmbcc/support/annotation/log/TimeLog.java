package com.sitech.crmbcc.support.annotation.log;

import com.sitech.crmbcc.support.aspect.log.TimeLogAspect;
import com.sitech.crmbcc.support.handler.LogHandler;
import com.sitech.crmbcc.support.handler.log.DefaultTimeLogHandler;
import com.sitech.crmbcc.support.model.log.LogModel;
import com.sitech.crmbcc.support.model.log.TimeLogModel;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <p>耗时日志注解，配合 {@link TimeLogAspect}、{@link DefaultTimeLogHandler} 使用，放在方法上。
 *
 * <h3>注意：</h3>
 *
 * <ol>
 * <li>如果所有属性都不展示，且包括描述为 ""，将不会调用 {@link DefaultTimeLogHandler} 中的 {@link DefaultTimeLogHandler#handle(LogModel)} 方法
 * 和 {@link DefaultTimeLogHandler#errorHandle(LogModel)} 方法，即不会打印日志。
 *
 * <li>如果方法在执行期间发生异常，将永远不会打印耗时和结束时间。
 * </ol>
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see TimeLogAspect
 * @see LogHandler
 * @see DefaultTimeLogHandler
 * @see LogModel
 * @see TimeLogModel
 * @since 2022/8/10 20:13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeLog {

    /**
     * 描述
     */
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
