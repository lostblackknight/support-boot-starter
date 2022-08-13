package com.sitech.crmbcc.support.handler.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitech.crmbcc.support.annotation.log.TimeLog;
import com.sitech.crmbcc.support.handler.LogHandler;
import com.sitech.crmbcc.support.model.log.LogModel;
import com.sitech.crmbcc.support.model.log.TimeLogModel;
import com.sitech.crmbcc.support.properties.LogProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认的耗时日志处理器。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see TimeLog
 * @see LogHandler
 * @see LogModel
 * @see TimeLogModel
 * @since 2022/8/11 20:57
 */
@Slf4j
public class DefaultTimeLogHandler implements LogHandler {

    private final ObjectMapper objectMapper;

    private final LogProperties.TimeLog timeLog;

    public DefaultTimeLogHandler(ObjectMapper objectMapper, LogProperties.TimeLog timeLog) {
        this.objectMapper = objectMapper;
        this.timeLog = timeLog;
    }

    @Override
    public void handle(LogModel model) throws Throwable {
        if (model instanceof TimeLogModel) {
            log.info(objectMapper.writeValueAsString(model));
        }
    }

    @Override
    public void errorHandle(LogModel model) throws Throwable {
        if (model instanceof TimeLogModel) {
            log.error(objectMapper.writeValueAsString(model));
        }
    }

    @Override
    public int getOrder() {
        return timeLog.getDefaultLogHandlerOrder();
    }
}
