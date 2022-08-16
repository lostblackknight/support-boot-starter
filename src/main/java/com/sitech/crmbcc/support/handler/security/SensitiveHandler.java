package com.sitech.crmbcc.support.handler.security;

import org.springframework.core.Ordered;

import java.util.Map;

/**
 * 敏感信息处理器，实现了 {@link Ordered} 接口，支持处理器的排序。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see Ordered
 * @since 2022/8/13 16:28
 */
public interface SensitiveHandler extends Ordered {

    /**
     * 处理返回值
     *
     * @param retValCopy    原来返回值的拷贝
     * @param processed     处理后的返回值
     * @param tag           标记
     * @param sendMessages  发送的消息
     * @param messageBroker 消息代理
     * @return 处理后的结果
     */
    default Object handle(Object retValCopy,
                          Object processed,
                          String tag,
                          Map<String, Object> sendMessages,
                          Map<String, Map<String, Object>> messageBroker) throws Throwable {
        return processed;
    }

    @Override
    default int getOrder() {
        return 0;
    }
}
