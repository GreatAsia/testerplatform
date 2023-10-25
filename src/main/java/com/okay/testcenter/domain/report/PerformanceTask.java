package com.okay.testcenter.domain.report;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class PerformanceTask {

    /**
     * 对应 performance_task 表
     */

    private int id;

    @NotNull(message = "app/rom 内容不能为空")
    @NotBlank(message = "app/rom 内容不能为空")
    private String client;

    @NotNull(message = "内存/耗电量/流量/启动时间/cpu/帧率 内容不能为空")
    @NotBlank(message = "内存/耗电量/流量/启动时间/cpu/帧率 内容不能为空")
    private String type;

    @NotNull(message = "序列号不能为空")
    @NotBlank(message = "序列号不能为空")
    private String serialno;



    @NotNull(message = "性能测试值不能为空")
    @NotBlank(message = "性能测试值不能为空")
    private String unit;


    @NotNull(message = "参考结果不能为空")
    @NotBlank(message = "参考结果不能为空")
    private String result;

    @NotNull(message = "版本号不能为空")
    @NotBlank(message = "版本号不能为空")
    private String version;

    private String totalTime;



}
