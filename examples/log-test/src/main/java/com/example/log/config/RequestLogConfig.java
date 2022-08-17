package com.example.log.config;

import com.sitech.crmbcc.support.handler.log.LogHandler;
import com.sitech.crmbcc.support.model.log.LogModel;
import com.sitech.crmbcc.support.model.log.RequestLogModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/17 8:54
 */
@Configuration
public class RequestLogConfig {

    @Bean
    @Order(1)
    public LogHandler myRequestLogHandler1() {
        return new LogHandler() {
            @Override
            public void handle(LogModel model) throws Throwable {
                if (model instanceof RequestLogModel) {
                    System.out.println("myRequestLogHandler1.handle" + model);
                }
            }

            @Override
            public void errorHandle(LogModel model) throws Throwable {
                if (model instanceof RequestLogModel) {
                    System.out.println("myRequestLogHandler1.errorHandle" + model);
                }
            }
        };
    }

    @Bean
    public LogHandler myRequestLogHandler2() {
        return new LogHandler() {
            @Override
            public void handle(LogModel model) throws Throwable {
                if (model instanceof RequestLogModel) {
                    System.out.println("myRequestLogHandler2.handle" + model);
                }
            }

            @Override
            public void errorHandle(LogModel model) throws Throwable {
                if (model instanceof RequestLogModel) {
                    System.out.println("myRequestLogHandler2.errorHandle" + model);
                }
            }

            @Override
            public int getOrder() {
                return 2;
            }
        };
    }

    @Component
    public static class MyRequestLogHandler3 implements LogHandler {

        @Override
        public void handle(LogModel model) throws Throwable {
            if (model instanceof RequestLogModel) {
                System.out.println("MyRequestLogHandler3.handle" + model);
            }
        }

        @Override
        public void errorHandle(LogModel model) throws Throwable {
            if (model instanceof RequestLogModel) {
                System.out.println("MyRequestLogHandler3.errorHandle" + model);
            }
        }

        @Override
        public int getOrder() {
            return 3;
        }
    }

//    @Bean(LogAutoConfiguration.REQUEST_LOG_HANDLER_BEAN_NAME)
//    public LogHandler requestLogHandler() {
//        return new LogHandler() {
//            @Override
//            public void handle(LogModel model) throws Throwable {
//                if (model instanceof RequestLogModel) {
//                    // 覆盖默认配置
//                }
//            }
//
//            @Override
//            public void errorHandle(LogModel model) throws Throwable {
//                if (model instanceof RequestLogModel) {
//                    // 覆盖默认配置
//                }
//            }
//        };
//    }
}
