package com.example.log.config;

import com.sitech.crmbcc.support.handler.log.LogHandler;
import com.sitech.crmbcc.support.model.log.LogModel;
import com.sitech.crmbcc.support.model.log.TimeLogModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/17 8:54
 */
@Configuration
public class TimeLogConfig {

    @Bean
    @Order(1)
    public LogHandler myTimeLogHandler1() {
        return new LogHandler() {
            @Override
            public void handle(LogModel model) throws Throwable {
                if (model instanceof TimeLogModel) {
                    System.out.println("myTimeLogHandler1.handle" + model);
                }
            }

            @Override
            public void errorHandle(LogModel model) throws Throwable {
                if (model instanceof TimeLogModel) {
                    System.out.println("myTimeLogHandler1.errorHandle" + model);
                }
            }
        };
    }

    @Bean
    public LogHandler myTimeLogHandler2() {
        return new LogHandler() {
            @Override
            public void handle(LogModel model) throws Throwable {
                if (model instanceof TimeLogModel) {
                    System.out.println("myTimeLogHandler2.handle" + model);
                }
            }

            @Override
            public void errorHandle(LogModel model) throws Throwable {
                if (model instanceof TimeLogModel) {
                    System.out.println("myTimeLogHandler2.errorHandle" + model);
                }
            }

            @Override
            public int getOrder() {
                return 2;
            }
        };
    }

    @Component
    public static class MyTimeLogHandler3 implements LogHandler {

        @Override
        public void handle(LogModel model) throws Throwable {
            if (model instanceof TimeLogModel) {
                System.out.println("MyTimeLogHandler3.handle" + model);
            }
        }

        @Override
        public void errorHandle(LogModel model) throws Throwable {
            if (model instanceof TimeLogModel) {
                System.out.println("MyTimeLogHandler3.errorHandle" + model);
            }
        }

        @Override
        public int getOrder() {
            return 3;
        }
    }

//    @Bean(LogAutoConfiguration.TIME_LOG_HANDLER_BEAN_NAME)
//    public LogHandler timeLogHandler() {
//        return new LogHandler() {
//            @Override
//            public void handle(LogModel model) throws Throwable {
//                if (model instanceof TimeLogModel) {
//                    // 覆盖默认配置
//                }
//            }
//
//            @Override
//            public void errorHandle(LogModel model) throws Throwable {
//                if (model instanceof TimeLogModel) {
//                    // 覆盖默认配置
//                }
//            }
//        };
//    }
}
