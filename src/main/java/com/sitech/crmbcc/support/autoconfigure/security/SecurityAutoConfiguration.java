package com.sitech.crmbcc.support.autoconfigure.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sitech.crmbcc.support.aspect.security.SensitiveAspect;
import com.sitech.crmbcc.support.handler.security.ResponseEntityDownStreamSensitiveHandler;
import com.sitech.crmbcc.support.handler.security.ResponseEntityUpStreamSensitiveHandler;
import com.sitech.crmbcc.support.handler.security.SensitiveHandler;
import com.sitech.crmbcc.support.properties.SecurityProperties;
import com.sitech.crmbcc.support.serializer.DesensitizedAnnotationSensitiveSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * 安全自动配置类。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see SecurityProperties
 * @see SensitiveAspect
 * @see SensitiveHandler
 * @see DesensitizedAnnotationSensitiveSerializer
 * @since 2022/8/13 16:34
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(AopAutoConfiguration.class)
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "support.security", name = "auto", havingValue = "true", matchIfMissing = true)
public class SecurityAutoConfiguration {

    private final SecurityProperties securityProperties;

    public static final String DESENSITIZED_ANNOTATION_SENSITIVE_SERIALIZER_BEAN_NAME = "desensitizedAnnotationSensitiveSerializer";

    public SecurityAutoConfiguration(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Configuration
    @ConditionalOnProperty(prefix = "support.security.sensitive", name = "enable", havingValue = "true", matchIfMissing = true)
    public static class JsonConfiguration {

        @Bean
        public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(
                DesensitizedAnnotationSensitiveSerializer desensitizedAnnotationSensitiveSerializer
        ) {
            return builder -> builder.serializerByType(String.class, desensitizedAnnotationSensitiveSerializer)
                    .mixIn(ResponseEntity.class, ResponseEntityMixin.class)
                    .mixIn(HttpStatus.class, HttpStatusMixIn.class);
        }

        @Bean
        @ConditionalOnMissingBean(name = DESENSITIZED_ANNOTATION_SENSITIVE_SERIALIZER_BEAN_NAME)
        public DesensitizedAnnotationSensitiveSerializer desensitizedAnnotationSensitiveSerializer() {
            return new DesensitizedAnnotationSensitiveSerializer();
        }

        // 处理 ResponseEntity 的反序列化
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ResponseEntityMixin {

            @JsonCreator
            public ResponseEntityMixin(@JsonProperty("body") Object body,
                                       @JsonDeserialize(as = LinkedMultiValueMap.class) @JsonProperty("headers") MultiValueMap<String, String> headers,
                                       @JsonProperty("statusCodeValue") HttpStatus status) {
            }
        }

        // 处理 HttpStatus 的反序列化
        public static class HttpStatusMixIn {

            @JsonCreator
            public static HttpStatus resolve(int statusCode) {
                return HttpStatus.NO_CONTENT;
            }
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "support.security.sensitive", name = "enable", havingValue = "true", matchIfMissing = true)
    public class SensitiveAutoConfiguration {

        private final ObjectMapper objectMapper;

        public SensitiveAutoConfiguration(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Bean
        public SensitiveAspect sensitiveAspect(List<SensitiveHandler> sensitiveHandlers) {
            return new SensitiveAspect(sensitiveHandlers, securityProperties.getSensitive(), objectMapper);
        }

        @Bean
        public ResponseEntityUpStreamSensitiveHandler responseEntityUpStreamSensitiveHandler() {
            return new ResponseEntityUpStreamSensitiveHandler();
        }

        @Bean
        public ResponseEntityDownStreamSensitiveHandler responseEntityDownStreamSensitiveHandler() {
            return new ResponseEntityDownStreamSensitiveHandler();
        }
    }
}
