package com.sitech.crmbcc.support.handler.security;

import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * {@link ResponseEntity} 类型的上游敏感信息处理器。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see SensitiveHandler
 * @see ResponseEntity
 * @since 2022/8/16 9:27
 */
public class ResponseEntityUpStreamSensitiveHandler implements SensitiveHandler {

    @Override
    public Object handle(Object retValCopy, Object processed, String tag, Map<String, Object> sendMessages, Map<String, Map<String, Object>> messageBroker) throws Throwable {
        if (processed instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) processed;
            final Object body = responseEntity.getBody();
            sendMessages.put("handle", true);
            return body;
        }
        return processed;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
