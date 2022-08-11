package com.sitech.crmbcc.support.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitech.crmbcc.support.annotation.RequestLog;
import com.sitech.crmbcc.support.util.RequestContextUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 请求日志切面
 * <p>相关：{@link RequestLog}
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/10 10:39
 */
@Slf4j
@Aspect
public class RequestLogAspect {

    @Around("@within(com.sitech.crmbcc.support.annotation.RequestLog)" +
            " || @annotation(com.sitech.crmbcc.support.annotation.RequestLog)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        final long start = System.currentTimeMillis();

        final Object target = pjp.getTarget();
        final Object[] args = pjp.getArgs();
        final MethodSignature signature = (MethodSignature) pjp.getSignature();
        final String[] parameterNames = signature.getParameterNames();

        final RequestLog requestLogOnClass = AnnotationUtils.getAnnotation(target.getClass(), RequestLog.class);
        final RequestLog requestLogOnMethod = AnnotationUtils.getAnnotation(signature.getMethod(), RequestLog.class);

        final Map<Object, Object> requestParam = new LinkedHashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            requestParam.put(parameterNames[i], args[i]);
        }
        final String ipAddress = RequestContextUtils.getIpAddress();
        final String method = RequestContextUtils.getMethod();
        final String requestURI = RequestContextUtils.getRequestURI();
        final String className = target.getClass().getCanonicalName();
        final String methodName = signature.getMethod().getName();

        final RequestLogModel model = new RequestLogModel();

        if (!ObjectUtils.isEmpty(requestLogOnClass)) {
            // 类上有注解
            if (StringUtils.hasLength(requestLogOnClass.description())) {
                // 有描述
                model.setDescription(requestLogOnClass.description());
            }

            if (!ObjectUtils.isEmpty(requestLogOnMethod)) {
                // 方法上有注解
                if (StringUtils.hasLength(requestLogOnMethod.description())) {
                    // 有描述
                    if (StringUtils.hasLength(model.getDescription())) {
                        model.setDescription(model.getDescription() + " | " + requestLogOnMethod.description());
                    } else {
                        model.setDescription(requestLogOnMethod.description());
                    }
                }
                buildRequestLogModel(requestLogOnMethod, model, requestParam, ipAddress, method, requestURI, className, methodName);
            } else {
                // 方法上没有注解
                buildRequestLogModel(requestLogOnClass, model, requestParam, ipAddress, method, requestURI, className, methodName);
            }
        } else {
            // 类上没注解
            if (!ObjectUtils.isEmpty(requestLogOnMethod)) {
                // 方法上有注解
                if (StringUtils.hasLength(requestLogOnMethod.description())) {
                    // 有描述
                    model.setDescription(requestLogOnMethod.description());
                }
                buildRequestLogModel(requestLogOnMethod, model, requestParam, ipAddress, method, requestURI, className, methodName);
            }
        }

        final Object retVal;
        try {
            retVal = pjp.proceed();

            final long end = System.currentTimeMillis();
            final String totalTime = (end - start) + "ms";

            if (!ObjectUtils.isEmpty(requestLogOnClass)) {
                // 类上有注解
                if (!ObjectUtils.isEmpty(requestLogOnMethod)) {
                    // 方法上有注解
                    if (requestLogOnMethod.showResponseParam()) {
                        model.setResponseParam(retVal);
                    }
                    if (requestLogOnMethod.showTotalTime()) {
                        model.setTotalTime(totalTime);
                    }
                } else {
                    // 方法上没有注解
                    if (requestLogOnClass.showResponseParam()) {
                        model.setResponseParam(retVal);
                    }
                    if (requestLogOnClass.showTotalTime()) {
                        model.setTotalTime(totalTime);
                    }
                }
                if (requestLogModelNotEmpty(model)) {
                    log.info(new ObjectMapper().writeValueAsString(model));
                }
            } else {
                // 类上没注解
                if (!ObjectUtils.isEmpty(requestLogOnMethod)) {
                    // 方法上有注解
                    if (requestLogOnMethod.showResponseParam()) {
                        model.setResponseParam(retVal);
                    }
                    if (requestLogOnMethod.showTotalTime()) {
                        model.setTotalTime(totalTime);
                    }
                    if (requestLogModelNotEmpty(model)) {
                        log.info(new ObjectMapper().writeValueAsString(model));
                    }
                }
            }
        } catch (Throwable e) {
            if (requestLogModelNotEmpty(model)) {
                log.error(new ObjectMapper().writeValueAsString(model));
            }
            throw e;
        }

        return retVal;
    }

    private boolean requestLogModelNotEmpty(RequestLogModel model) {
        return !ObjectUtils.isEmpty(model) && !model.equals(new RequestLogModel());
    }

    private void buildRequestLogModel(RequestLog requestLog,
                                      RequestLogModel model,
                                      Map<Object, Object> requestParam,
                                      String ipAddress,
                                      String method,
                                      String requestURI,
                                      String className,
                                      String methodName) {
        if (requestLog.showIpAddress()) {
            model.setIpAddress(ipAddress);
        }
        if (requestLog.showMethod()) {
            model.setMethod(method);
        }
        if (requestLog.showRequestURI()) {
            model.setRequestURI(requestURI);
        }
        if (requestLog.showRequestParam()) {
            model.setRequestParam(requestParam);
        }
        if (requestLog.showClassName()) {
            model.setClassName(className);
        }
        if (requestLog.showMethodName()) {
            model.setMethodName(methodName);
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class RequestLogModel {
        private String description;
        private String ipAddress;
        private String method;
        private String requestURI;
        private Map<Object, Object> requestParam;
        private Object responseParam;
        private String className;
        private String methodName;
        private String totalTime;
    }
}
