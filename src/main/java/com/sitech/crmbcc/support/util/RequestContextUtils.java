package com.sitech.crmbcc.support.util;

import com.sitech.crmbcc.support.exception.RequestContextException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求上下文工具类
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/10 10:41
 */
public class RequestContextUtils {

    /**
     * 获取 HttpServletRequest 对象， 通过 {@link RequestContextHolder}
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getRequest();
        }
        throw new RequestContextException("当前请求的上下文不是 ServletContext");
    }

    /**
     * 获取 HttpServletResponse 对象， 通过 {@link RequestContextHolder}
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getResponse();
        }
        throw new RequestContextException("当前请求的上下文不是 ServletContext");
    }
}
