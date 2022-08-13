package com.sitech.crmbcc.support.properties;

import com.sitech.crmbcc.support.autoconfigure.LogAutoConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志配置属性。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see LogAutoConfiguration
 * @since 2022/8/11 22:20
 */
@Data
@ConfigurationProperties("support.log")
public class LogProperties {

    /**
     * 是否开启日志自动配置
     */
    private boolean auto = true;

    /**
     * 请求日志
     */
    private RequestLog request = new RequestLog();

    /**
     * 耗时日志
     */
    private TimeLog time = new TimeLog();

    @Data
    public static class RequestLog {

        /**
         * 是否开启 {@link com.sitech.crmbcc.support.annotation.log.RequestLog}
         */
        private boolean enable = true;

        /**
         * 请求日志切面排序
         */
        private int aspectOrder = 0;

        /**
         * 默认请求日志处理器排序
         */
        private int defaultLogHandlerOrder = 0;

        /**
         * 全局配置
         */
        private Global global = new Global();

        @Data
        public static class Global {

            /**
             * 是否展示 IP 地址
             */
            private boolean showIpAddress = true;

            /**
             * 是否展示请求的方法的类型
             */
            private boolean showMethod = true;

            /**
             * 是否展示请求的 URI
             */
            private boolean showRequestURI = true;

            /**
             * 是否展示请求参数
             */
            private boolean showRequestParam = true;

            /**
             * 是否展示响应参数
             */
            private boolean showResponseParam = true;

            /**
             * 是否展示请求的类的名称
             */
            private boolean showClassName = true;

            /**
             * 是否展示请求的方法的名称
             */
            private boolean showMethodName = true;

            /**
             * 是否展示请求的耗时
             */
            private boolean showTotalTime = true;
        }
    }

    @Data
    public static class TimeLog {

        /**
         * 是否开启 {@link com.sitech.crmbcc.support.annotation.log.TimeLog}
         */
        private boolean enable = true;

        /**
         * 耗时日志切面排序
         */
        private int aspectOrder = 0;

        /**
         * 默认耗时日志处理器排序
         */
        private int defaultLogHandlerOrder = 0;

        /**
         * 全局配置
         */
        private Global global = new Global();

        @Data
        public static class Global {

            /**
             * 是否展示类的名称
             */
            private boolean showClassName = true;

            /**
             * 是否展示方法的名称
             */
            private boolean showMethodName = true;

            /**
             * 是否展示开始时间
             */
            private boolean showStartTime = true;

            /**
             * 是否展示结束时间
             */
            private boolean showEndTime = true;

            /**
             * 是否展示耗时
             */
            private boolean showTotalTime = true;
        }
    }
}
