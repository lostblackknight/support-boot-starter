package com.sitech.crmbcc.support.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitech.crmbcc.support.annotation.TimeLog;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 耗时日志切面
 * <p>相关：{@link TimeLog}
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/10 20:15
 */
@Slf4j
@Aspect
public class TimeLogAspect {

    @Around("@annotation(com.sitech.crmbcc.support.annotation.TimeLog)")
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
            if (timeLog.showClassName()) {
                model.setClassName(className);
            }
            if (timeLog.showMethodName()) {
                model.setMethodName(methodName);
            }
            if (timeLog.showStartTime()) {
                model.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(start),
                        ZoneId.systemDefault()).toString());
            }
        }

        final Object retVal;
        try {
            retVal = pjp.proceed();

            final long end = System.currentTimeMillis();
            final String totalTime = (end - start) + "ms";

            if (!ObjectUtils.isEmpty(timeLog)) {
                if (timeLog.showEndTime()) {
                    model.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(end),
                            ZoneId.systemDefault()).toString());
                }
                if (timeLog.showTotalTime()) {
                    model.setTotalTime(totalTime);
                }
                if (timeLogModelNotEmpty(model)) {
                    log.info(new ObjectMapper().writeValueAsString(model));
                }
            }
        } catch (Throwable e) {
            if (timeLogModelNotEmpty(model)) {
                log.error(new ObjectMapper().writeValueAsString(model));
            }
            throw e;
        }
        return retVal;
    }

    private boolean timeLogModelNotEmpty(TimeLogModel model) {
        return !ObjectUtils.isEmpty(model) && !model.equals(new TimeLogModel());
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class TimeLogModel {
        private String description;
        private String className;
        private String methodName;
        private String startTime;
        private String endTime;
        private String totalTime;
    }
}
