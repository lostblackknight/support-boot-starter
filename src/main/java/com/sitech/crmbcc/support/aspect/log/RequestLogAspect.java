package com.sitech.crmbcc.support.aspect.log;

import com.sitech.crmbcc.support.annotation.log.RequestLog;
import com.sitech.crmbcc.support.handler.LogHandler;
import com.sitech.crmbcc.support.model.log.RequestLogModel;
import com.sitech.crmbcc.support.properties.LogProperties;
import com.sitech.crmbcc.support.util.RequestContextUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求日志切面，实现了 {@link Ordered} 接口，支持切面的排序。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see RequestLog
 * @see Ordered
 * @since 2022/8/10 10:39
 */
@Aspect
public class RequestLogAspect implements Ordered {

    private final List<LogHandler> logHandlers;

    private final LogProperties.RequestLog requestLogProperties;

    public RequestLogAspect(List<LogHandler> logHandlers, LogProperties.RequestLog requestLogProperties) {
        this.logHandlers = logHandlers;
        this.requestLogProperties = requestLogProperties;
    }

    @Around("@within(com.sitech.crmbcc.support.annotation.log.RequestLog)" +
            " || @annotation(com.sitech.crmbcc.support.annotation.log.RequestLog)")
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
                    if (requestLogProperties.getGlobal().isShowResponseParam() && requestLogOnMethod.showResponseParam()) {
                        model.setResponseParam(retVal);
                    }
                    if (requestLogProperties.getGlobal().isShowTotalTime() && requestLogOnMethod.showTotalTime()) {
                        model.setTotalTime(totalTime);
                    }
                } else {
                    // 方法上没有注解
                    if (requestLogProperties.getGlobal().isShowResponseParam() && requestLogOnClass.showResponseParam()) {
                        model.setResponseParam(retVal);
                    }
                    if (requestLogProperties.getGlobal().isShowTotalTime() && requestLogOnClass.showTotalTime()) {
                        model.setTotalTime(totalTime);
                    }
                }
                if (requestLogModelNotEmpty(model)) {
                    for (LogHandler logHandler : logHandlers) {
                        logHandler.handle(model);
                    }
                }
            } else {
                // 类上没注解
                if (!ObjectUtils.isEmpty(requestLogOnMethod)) {
                    // 方法上有注解
                    if (requestLogProperties.getGlobal().isShowResponseParam() && requestLogOnMethod.showResponseParam()) {
                        model.setResponseParam(retVal);
                    }
                    if (requestLogProperties.getGlobal().isShowTotalTime() && requestLogOnMethod.showTotalTime()) {
                        model.setTotalTime(totalTime);
                    }
                    if (requestLogModelNotEmpty(model)) {
                        for (LogHandler logHandler : logHandlers) {
                            logHandler.handle(model);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            if (requestLogModelNotEmpty(model)) {
                for (LogHandler logHandler : logHandlers) {
                    logHandler.errorHandle(model);
                }
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
        if (requestLogProperties.getGlobal().isShowIpAddress() && requestLog.showIpAddress()) {
            model.setIpAddress(ipAddress);
        }
        if (requestLogProperties.getGlobal().isShowMethod() && requestLog.showMethod()) {
            model.setMethod(method);
        }
        if (requestLogProperties.getGlobal().isShowRequestURI() && requestLog.showRequestURI()) {
            model.setRequestURI(requestURI);
        }
        if (requestLogProperties.getGlobal().isShowRequestParam() && requestLog.showRequestParam()) {
            model.setRequestParam(requestParam);
        }
        if (requestLogProperties.getGlobal().isShowClassName() && requestLog.showClassName()) {
            model.setClassName(className);
        }
        if (requestLogProperties.getGlobal().isShowMethodName() && requestLog.showMethodName()) {
            model.setMethodName(methodName);
        }
    }

    @Override
    public int getOrder() {
        return requestLogProperties.getAspectOrder();
    }
}
