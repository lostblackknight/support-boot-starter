package com.sitech.crmbcc.support.aspect.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitech.crmbcc.support.annotation.security.Sensitive;
import com.sitech.crmbcc.support.handler.security.SensitiveHandler;
import com.sitech.crmbcc.support.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感信息切面，实现了 {@link Ordered} 接口，支持切面的排序。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see Sensitive
 * @see Ordered
 * @since 2022/8/13 15:07
 */
@Slf4j
@Aspect
public class SensitiveAspect implements Ordered {

    private final List<SensitiveHandler> sensitiveHandlers;

    private final SecurityProperties.Sensitive sensitiveProperties;

    private final ObjectMapper objectMapper;

    public SensitiveAspect(List<SensitiveHandler> sensitiveHandlers, SecurityProperties.Sensitive sensitiveProperties, ObjectMapper objectMapper) {
        this.sensitiveHandlers = sensitiveHandlers;
        this.sensitiveProperties = sensitiveProperties;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(com.sitech.crmbcc.support.annotation.security.Sensitive)")
    Object around(ProceedingJoinPoint pjp) throws Throwable {
        final MethodSignature signature = (MethodSignature) pjp.getSignature();
        final Sensitive sensitive = AnnotationUtils.findAnnotation(signature.getMethod(), Sensitive.class);

        Object processed = pjp.proceed();

        if (!ObjectUtils.isEmpty(sensitive)) {
            final String tag = sensitive.tag();

            if (!ObjectUtils.isEmpty(processed) && notPrimitive(processed)) {
                // 使用 objectMapper 进行深拷贝
                Object retValCopy = objectMapper.readValue(objectMapper.writeValueAsString(processed), processed.getClass());
                // 消息代理
                Map<String, Map<String, Object>> messageBroker = new HashMap<>();

                for (SensitiveHandler sensitiveHandler : sensitiveHandlers) {
                    final Map<String, Object> messages = new HashMap<>();
                    processed = sensitiveHandler.handle(retValCopy, processed, tag, messages, messageBroker);
                    messageBroker.put(sensitiveHandler.getClass().getSimpleName(), messages);
                }

                messageBroker.clear();
            }
        }

        return processed;
    }

    public static boolean notPrimitive(Object obj) {
        try {
            // byte short int long float double boolean char void
            final Class<?> type = (Class<?>) obj.getClass().getDeclaredField("TYPE").get(obj);
            if (type.isPrimitive()) {
                if (log.isDebugEnabled()) {
                    log.debug("返回值为原始类型: {}", obj.getClass());
                }
                return false;
            } else {
                return true;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return true;
        }
    }

    @Override
    public int getOrder() {
        return sensitiveProperties.getAspectOrder();
    }
}
