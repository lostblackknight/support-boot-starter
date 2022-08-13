package com.sitech.crmbcc.support.model.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

/**
 * 请求日志模型。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see LogModel
 * @since 2022/8/11 19:59
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestLogModel implements LogModel {

    /**
     * 描述
     */
    private String description;

    /**
     * IP 地址
     */
    private String ipAddress;

    /**
     * 请求的方法的类型
     */
    private String method;

    /**
     * 请求的 URI
     */
    private String requestURI;

    /**
     * 请求参数
     */
    private Map<Object, Object> requestParam;

    /**
     * 响应参数
     */
    private Object responseParam;

    /**
     * 请求的类的名称
     */
    private String className;

    /**
     * 请求的方法的名称
     */
    private String methodName;

    /**
     * 请求的耗时
     */
    private String totalTime;
}
