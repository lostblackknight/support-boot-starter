package com.sitech.crmbcc.support.serializer;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.sitech.crmbcc.support.annotation.security.Desensitized;
import com.sitech.crmbcc.support.enums.security.DesensitizedType;

import java.io.IOException;
import java.util.Objects;

/**
 * 默认的脱敏注解的敏感信息序列化器。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see Desensitized
 * @since 2022/8/15 8:41
 */
public class DesensitizedAnnotationSensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private DesensitizedType desensitizedType;

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null) {
            if (Objects.equals(property.getType().getRawClass(), String.class)) {
                Desensitized desensitized = property.getAnnotation(Desensitized.class);
                if (desensitized == null) {
                    desensitized = property.getContextAnnotation(Desensitized.class);
                }
                if (desensitized != null) {
                    try {
                        return getSerializer(desensitized);
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                return new StringSerializer();
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(null);
    }

    protected JsonSerializer<?> getSerializer(Desensitized desensitized) throws InstantiationException, IllegalAccessException {
        final DesensitizedAnnotationSensitiveSerializer serializer = this.getClass().newInstance();
        serializer.setDesensitizedType(desensitized.value());
        return serializer;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        switch (desensitizedType) {
            case CHINESE_NAME:
                this.processChineseName(value, gen, serializers);
                break;
            case ID_CARD:
                this.processIdCard(value, gen, serializers);
                break;
            case FIXED_PHONE:
                this.processFixedPhone(value, gen, serializers);
                break;
            case MOBILE_PHONE:
                this.processMobilePhone(value, gen, serializers);
                break;
            case ADDRESS:
                this.processAddress(value, gen, serializers);
                break;
            case EMAIL:
                this.processEmail(value, gen, serializers);
                break;
            case PASSWORD:
                this.processPassword(value, gen, serializers);
                break;
            case CAR_LICENSE:
                this.processCarLicense(value, gen, serializers);
                break;
            case BANK_CARD:
                this.processBankCard(value, gen, serializers);
                break;
            default:
                gen.writeString(value);
                break;
        }
    }

    protected void processBankCard(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DesensitizedUtil.bankCard(value));
    }

    protected void processCarLicense(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DesensitizedUtil.carLicense(value));
    }

    protected void processPassword(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DesensitizedUtil.password(value));
    }

    protected void processEmail(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DesensitizedUtil.email(value));
    }

    protected void processAddress(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DesensitizedUtil.address(value, 8));
    }

    protected void processMobilePhone(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DesensitizedUtil.mobilePhone(value));
    }

    protected void processFixedPhone(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DesensitizedUtil.fixedPhone(value));
    }

    protected void processIdCard(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DesensitizedUtil.idCardNum(value, 1, 2));
    }

    protected void processChineseName(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DesensitizedUtil.chineseName(value));
    }

    public DesensitizedType getDesensitizedType() {
        return desensitizedType;
    }

    public void setDesensitizedType(DesensitizedType desensitizedType) {
        this.desensitizedType = desensitizedType;
    }
}
