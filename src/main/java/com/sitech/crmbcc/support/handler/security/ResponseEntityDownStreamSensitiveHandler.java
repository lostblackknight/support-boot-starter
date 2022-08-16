package com.sitech.crmbcc.support.handler.security;

import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * {@link ResponseEntity} 类型的下游敏感信息处理器。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see SensitiveHandler
 * @see ResponseEntity
 * @since 2022/8/16 9:27
 */
public class ResponseEntityDownStreamSensitiveHandler implements SensitiveHandler {

    @Override
    public Object handle(Object retValCopy, Object processed, String tag, Map<String, Object> sendMessages, Map<String, Map<String, Object>> messageBroker) throws Throwable {
        final Map<String, Object> receivedMessages = messageBroker.get(ResponseEntityUpStreamSensitiveHandler.class.getSimpleName());
        if (receivedMessages.get("handle") != null && receivedMessages.get("handle").equals(true)) {
            ResponseEntity<?> responseEntityCopy = (ResponseEntity<?>) retValCopy;
            return ResponseEntity.status(responseEntityCopy.getStatusCode())
                    .headers(responseEntityCopy.getHeaders())
                    .body(processed);
        }
        return processed;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
