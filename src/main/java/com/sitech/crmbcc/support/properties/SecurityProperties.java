package com.sitech.crmbcc.support.properties;

import com.sitech.crmbcc.support.autoconfigure.security.SecurityAutoConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 安全配置属性。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see SecurityAutoConfiguration
 * @since 2022/8/13 16:56
 */
@Data
@ConfigurationProperties("support.security")
public class SecurityProperties {

    /**
     * 是否开启安全自动配置
     */
    private boolean auto = true;

    /**
     * 敏感信息
     */
    private Sensitive sensitive = new Sensitive();

    @Data
    public static class Sensitive {

        /**
         * 是否开启 {@link com.sitech.crmbcc.support.annotation.security.Sensitive}
         */
        private boolean enable = true;

        /**
         * 敏感信息切面排序
         */
        private int aspectOrder = 0;
    }
}
