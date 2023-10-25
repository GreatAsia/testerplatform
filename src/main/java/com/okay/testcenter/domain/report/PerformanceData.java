package com.okay.testcenter.domain.report;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PerformanceData {

    /**
     * 对应 performance_data 表
     */

    private int id;

    @NotNull(message = "任务ID不能为空")
    @Min(1)
    private int taskId;


    @NotNull(message = "性能数据不能为空")
    private float preSize;


    @NotNull(message = "运行时间不能为空")
    @NotBlank(message = "运行时间不能为空")
    private String runTime;




}
