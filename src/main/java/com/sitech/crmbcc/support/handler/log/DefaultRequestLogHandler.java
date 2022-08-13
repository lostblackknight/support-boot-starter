package com.sitech.crmbcc.support.handler.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitech.crmbcc.support.annotation.log.RequestLog;
import com.sitech.crmbcc.support.handler.LogHandler;
import com.sitech.crmbcc.support.model.log.LogModel;
import com.sitech.crmbcc.support.model.log.RequestLogModel;
import com.sitech.crmbcc.support.properties.LogProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的请求日志处理器。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see RequestLog
 * @see LogHandler
 * @see LogModel
 * @see RequestLogModel
 * @since 2022/8/11 20:52
 */
@Slf4j
public class DefaultRequestLogHandler implements LogHandler {

    private final ObjectMapper objectMapper;

    private final LogProperties.RequestLog requestLogProperties;

    public DefaultRequestLogHandler(ObjectMapper objectMapper, LogProperties.RequestLog requestLogProperties) {
        this.objectMapper = objectMapper;
        this.requestLogProperties = requestLogProperties;
    }

    @Override
    public void handle(LogModel model) throws Throwable {
        if (model instanceof RequestLogModel) {
            log.info(objectMapper.writeValueAsString(model));
        }
    }

    @Override
    public void errorHandle(LogModel model) throws Throwable {
        if (model instanceof RequestLogModel) {
            log.error(objectMapper.writeValueAsString(model));
        }
    }

    @Override
    public int getOrder() {
        return requestLogProperties.getDefaultLogHandlerOrder();
    }
}
