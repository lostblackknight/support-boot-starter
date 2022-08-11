package com.sitech.crmbcc.support.configure;

import com.sitech.crmbcc.support.aspect.RequestLogAspect;
import com.sitech.crmbcc.support.aspect.TimeLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志配置类
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/11 1:01
 */
@Configuration(proxyBeanMethods = false)
public class LogConfiguration {

    @Bean
    public RequestLogAspect requestLogAspect() {
        return new RequestLogAspect();
    }

    @Bean
    public TimeLogAspect timeLogAspect() {
        return new TimeLogAspect();
    }
}
