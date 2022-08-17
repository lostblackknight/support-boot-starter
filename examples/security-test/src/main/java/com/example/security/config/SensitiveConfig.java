package com.example.security.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/17 14:34
 */
@Configuration
public class SensitiveConfig {

//    @Component(SecurityAutoConfiguration.DESENSITIZED_ANNOTATION_SENSITIVE_SERIALIZER_BEAN_NAME)
//    public static class CustomerDesensitizedAnnotationSensitiveSerializer extends DesensitizedAnnotationSensitiveSerializer {
//
//        @Override
//        protected void processAddress(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//            gen.writeString(DesensitizedUtil.address(value, 4));
//        }
//    }
}
