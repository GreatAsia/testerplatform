package com.okay.testcenter.domain.report;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 对应 ui_pad_runidlist 表
 */

public class UiPadRunIdList {


    private int id;

    @NotNull(message = "设备总数不能为空")
    @Min(0)
    private int totalDevice;

    @NotNull(message = "通过设备数不能为空")
    @Min(0)
    private int passDevice;

    @NotNull(message = "失败设备数不能为空")
    @Min(0)
    private int failDevice;

    @NotNull(message = "错误设备数不能为空")
    @Min(0)
    private int errorDevice;

    @NotNull(message = "耗时不能为空")
    @NotBlank(message = "耗时不能为空")
    private String elapsedTime;

    @NotNull(message = "开始时间不能为空")
    @NotBlank(message = "开始时间不能为空")
    private String startTime;

    @NotNull(message = "通过率不能为空")
    @NotBlank(message = "通过率不能为空")
    private String passRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalDevice() {
        return totalDevice;
    }

    public void setTotalDevice(int totalDevice) {
        this.totalDevice = totalDevice;
    }

    public int getPassDevice() {
        return passDevice;
    }

    public void setPassDevice(int passDevice) {
        this.passDevice = passDevice;
    }

    public int getFailDevice() {
        return failDevice;
    }

    public void setFailDevice(int failDevice) {
        this.failDevice = failDevice;
    }

    public int getErrorDevice() {
        return errorDevice;
    }

    public void setErrorDevice(int errorDevice) {
        this.errorDevice = errorDevice;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPassRate() {
        return passRate;
    }

    public void setPassRate(String passRate) {
        this.passRate = passRate;
    }

}
