package com.sitech.crmbcc.support.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * IP 工具类
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/10 11:02
 */
public class IPUtils {

    private static final String UNKNOWN = "unknown";

    private static final String LOCALHOST_IPV4 = "127.0.0.1";

    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };

    /**
     * 获取客户端 IP
     *
     * @param request 请求对象 {@link HttpServletRequest}
     * @return IP 地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String realIP = UNKNOWN;
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (StringUtils.hasLength(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                realIP = ip;
                break;
            }
        }
        if (UNKNOWN.equals(realIP)) {
            realIP = request.getRemoteAddr();
        }

        // However, if multi-level reverse proxy is adopted, the value of X-Forwarded-For is not only one,
        // but a series of IP values. Which is the real IP of the client?
        // The answer is to take the first valid IP string in X-Forwarded-For that is not unknown. Such as:
        // X-Forwarded-For: 192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
        // User real IP: 192.168.1.110

        return LOCALHOST_IPV6.equals(realIP) ? LOCALHOST_IPV4 : getMultistageReverseProxyIpAddress(realIP);
    }

    /**
     * 从多级反向代理中获得第一个非 unknown IP 地址
     *
     * @param ip 获得的 IP 地址
     * @return 第一个非 unknown IP 地址
     */
    public static String getMultistageReverseProxyIpAddress(String ip) {
        if (ip != null && ip.indexOf(",") > 0) {
            final String[] ips = ip.trim().split(",");
            for (String subIp : ips) {
                if (StringUtils.hasLength(subIp) && !UNKNOWN.equalsIgnoreCase(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }
}
