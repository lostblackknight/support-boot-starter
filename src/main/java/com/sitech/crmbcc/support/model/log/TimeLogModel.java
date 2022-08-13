package com.sitech.crmbcc.support.model.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 耗时日志模型。
 *
 * @author chensixiang (chensixiang1234@gmail.com)
 * @see LogModel
 * @since 2022/8/11 19:58
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeLogModel implements LogModel {

    /**
     * 描述
     */
    private String description;

    /**
     * 类的名称
     */
    private String className;

    /**
     * 方法的名称
     */
    private String methodName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 耗时
     */
    private String totalTime;
}
