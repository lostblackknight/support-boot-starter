package com.sitech.crmbcc.support.exception;

/**
 * 请求上下文异常。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/10 11:09
 */
public class RequestContextException extends RuntimeException {

    public RequestContextException() {
    }

    public RequestContextException(String message) {
        super(message);
    }

    public RequestContextException(Throwable cause) {
        super(cause);
    }

    public RequestContextException(String message, Throwable cause) {
        super(message, cause);
    }
}
