package com.sitech.crmbcc.support.annotation;

import com.sitech.crmbcc.support.configure.LogConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启启日志功能 {@link RequestLog}
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/11 1:09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogConfiguration.class)
public @interface EnableLog {
}
