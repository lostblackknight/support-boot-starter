package com.sitech.crmbcc.support.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitech.crmbcc.support.aspect.log.RequestLogAspect;
import com.sitech.crmbcc.support.aspect.log.TimeLogAspect;
import com.sitech.crmbcc.support.handler.LogHandler;
import com.sitech.crmbcc.support.handler.log.DefaultRequestLogHandler;
import com.sitech.crmbcc.support.handler.log.DefaultTimeLogHandler;
import com.sitech.crmbcc.support.properties.LogProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 日志自动配置类。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see LogProperties
 * @see RequestLogAspect
 * @see DefaultRequestLogHandler
 * @see TimeLogAspect
 * @see DefaultRequestLogHandler
 * @since 2022/8/11 23:01
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({AopAutoConfiguration.class})
@EnableConfigurationProperties({LogProperties.class})
@ConditionalOnProperty(prefix = "support.log", name = "auto", havingValue = "true", matchIfMissing = true)
public class LogAutoConfiguration {

    private final LogProperties logProperties;

    private final ObjectMapper objectMapper;

    public LogAutoConfiguration(LogProperties logProperties, ObjectMapper objectMapper) {
        this.logProperties = logProperties;
        this.objectMapper = objectMapper;
    }

    @Configuration
    @ConditionalOnProperty(prefix = "support.log.request", name = "enable", havingValue = "true", matchIfMissing = true)
    public class RequestLogAutoConfiguration {

        @Bean
        public RequestLogAspect requestLogAspect(List<LogHandler> logHandlers) {
            return new RequestLogAspect(logHandlers, logProperties.getRequest());
        }

        @Bean
        @ConditionalOnMissingBean(name = "requestLogHandler")
        public DefaultRequestLogHandler requestLogHandler() {
            return new DefaultRequestLogHandler(objectMapper, logProperties.getRequest());
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "support.log.time", name = "enable", havingValue = "true", matchIfMissing = true)
    public class TimeLogAutoConfiguration {

        @Bean
        public TimeLogAspect timeLogAspect(List<LogHandler> logHandlers) {
            return new TimeLogAspect(logHandlers, logProperties.getTime());
        }

        @Bean
        @ConditionalOnMissingBean(name = "timeLogHandler")
        public DefaultTimeLogHandler timeLogHandler() {
            return new DefaultTimeLogHandler(objectMapper, logProperties.getTime());
        }
    }
}
