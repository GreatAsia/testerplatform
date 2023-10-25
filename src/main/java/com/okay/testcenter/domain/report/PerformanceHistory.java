package com.okay.testcenter.domain.report;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class PerformanceHistory {

    /**
     * 对应 performance_history 表
     */

    private int id;

    @NotNull(message = "运行ID不能为空")
    @Min(1)
    private int runId;

    @NotNull(message = "app/rom 内容不能为空")
    @NotBlank(message = "app/rom 内容不能为空")
    private String name;

    @NotNull(message = "内存/耗电量/流量/启动时间/cpu/帧率 内容不能为空")
    @NotBlank(message = "内存/耗电量/流量/启动时间/cpu/帧率 内容不能为空")
    private String type;

    @NotNull(message = "序列号不能为空")
    @NotBlank(message = "序列号不能为空")
    private String serialno;

    @NotNull(message = "单位不能为空")
    private float preSize;

    @NotNull(message = "性能测试值不能为空")
    @NotBlank(message = "性能测试值不能为空")
    private String unit;

    @NotNull(message = "运行时间不能为空")
    @NotBlank(message = "运行时间不能为空")
    private String runTime;

    @NotNull(message = "参考结果不能为空")
    @NotBlank(message = "参考结果不能为空")
    private String result;

    private ArrayList preSizes;
    private ArrayList runTimes;
    private int total;

    private String version;
    private String totalTime;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public float getPreSize() {
        return preSize;
    }

    public void setPreSize(float preSize) {
        this.preSize = preSize;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public ArrayList getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(ArrayList runTimes) {
        this.runTimes = runTimes;
    }

    public ArrayList getPreSizes() {
        return preSizes;
    }

    public void setPreSizes(ArrayList preSizes) {
        this.preSizes = preSizes;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }


}
