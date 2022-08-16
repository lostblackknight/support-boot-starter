package com.sitech.crmbcc.support.enums.security;

import com.sitech.crmbcc.support.annotation.security.Desensitized;

/**
 * 支持的脱敏类型枚举。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see Desensitized
 * @since 2022/8/13 19:52
 */
public enum DesensitizedType {

    /**
     * 中文名
     */
    CHINESE_NAME,

    /**
     * 身份证号
     */
    ID_CARD,

    /**
     * 座机号
     */
    FIXED_PHONE,

    /**
     * 手机号
     */
    MOBILE_PHONE,

    /**
     * 地址
     */
    ADDRESS,

    /**
     * 电子邮件
     */
    EMAIL,

    /**
     * 密码
     */
    PASSWORD,

    /**
     * 中国大陆车牌，包含普通车辆、新能源车辆
     */
    CAR_LICENSE,

    /**
     * 银行卡
     */
    BANK_CARD
}
