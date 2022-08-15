package com.sitech.crmbcc.support.handler.log;

import com.sitech.crmbcc.support.model.log.LogModel;
import org.springframework.core.Ordered;

/**
 * 日志处理器，实现了 {@link Ordered} 接口，支持处理器的排序。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see Ordered
 * @since 2022/8/11 20:51
 */
public interface LogHandler extends Ordered {

    /**
     * 处理日志
     *
     * @param model 日志模型
     */
    void handle(LogModel model) throws Throwable;

    /**
     * 处理错误日志
     *
     * @param model 日志模型
     */
    void errorHandle(LogModel model) throws Throwable;

    @Override
    default int getOrder() {
        return 0;
    }
}
