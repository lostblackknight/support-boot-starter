package com.sitech.crmbcc.support.aspect.log;

import com.sitech.crmbcc.support.annotation.log.TimeLog;
import com.sitech.crmbcc.support.handler.LogHandler;
import com.sitech.crmbcc.support.model.log.TimeLogModel;
import com.sitech.crmbcc.support.properties.LogProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 耗时日志切面，实现了 {@link Ordered} 接口，支持切面的排序。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see TimeLog
 * @see Ordered
 * @since 2022/8/10 20:15
 */
@Aspect
public class TimeLogAspect implements Ordered {

    private final List<LogHandler> logHandlers;

    private final LogProperties.TimeLog timeLogProperties;

    public TimeLogAspect(List<LogHandler> logHandlers, LogProperties.TimeLog timeLogProperties) {
        this.logHandlers = logHandlers;
        this.timeLogProperties = timeLogProperties;
    }

    @Around("@annotation(com.sitech.crmbcc.support.annotation.log.TimeLog)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        final long start = System.currentTimeMillis();

        final Object target = pjp.getTarget();
        final MethodSignature signature = (MethodSignature) pjp.getSignature();

        final TimeLog timeLog = AnnotationUtils.findAnnotation(signature.getMethod(), TimeLog.class);

        final String className = target.getClass().getCanonicalName();
        final String methodName = signature.getMethod().getName();

        final TimeLogModel model = new TimeLogModel();

        if (!ObjectUtils.isEmpty(timeLog)) {
            if (StringUtils.hasLength(timeLog.description())) {
                model.setDescription(timeLog.description());
            }
            if (timeLogProperties.getGlobal().isShowClassName() && timeLog.showClassName()) {
                model.setClassName(className);
            }
            if (timeLogProperties.getGlobal().isShowMethodName() && timeLog.showMethodName()) {
                model.setMethodName(methodName);
            }
            if (timeLogProperties.getGlobal().isShowStartTime() && timeLog.showStartTime()) {
                model.setStartTime(new Date(start));
            }
        }

        final Object retVal;

        try {
            retVal = pjp.proceed();

            final long end = System.currentTimeMillis();
            final String totalTime = (end - start) + "ms";

            if (!ObjectUtils.isEmpty(timeLog)) {
                if (timeLogProperties.getGlobal().isShowEndTime() && timeLog.showEndTime()) {
                    model.setEndTime(new Date(end));
                }
                if (timeLogProperties.getGlobal().isShowTotalTime() && timeLog.showTotalTime()) {
                    model.setTotalTime(totalTime);
                }
                if (timeLogModelNotEmpty(model)) {
                    for (LogHandler logHandler : logHandlers) {
                        logHandler.handle(model);
                    }
                }
            }
        } catch (Throwable e) {
            if (timeLogModelNotEmpty(model)) {
                for (LogHandler logHandler : logHandlers) {
                    logHandler.errorHandle(model);
                }
            }
            throw e;
        }
        return retVal;
    }

    private boolean timeLogModelNotEmpty(TimeLogModel model) {
        return !ObjectUtils.isEmpty(model) && !model.equals(new TimeLogModel());
    }

    @Override
    public int getOrder() {
        return timeLogProperties.getAspectOrder();
    }
}
