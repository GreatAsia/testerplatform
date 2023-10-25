package com.okay.testcenter.domain.report;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 对应 ui_pad_serialnolist 表
 */
public class UiPadSerialnoList {


    private int id;

    @NotNull(message = "运行ID不能为空")
    @Min(1)
    private int runId;

    @NotNull(message = "序列号不能为空")
    @NotBlank(message = "序列号不能为空")
    private String serialno;

    @NotNull(message = "版本号不能为空")
    @NotBlank(message = "版本号不能为空")
    private String version;

    @NotNull(message = "用例总数不能为空")
    @Min(1)
    private int totalCase;

    @NotNull(message = "通过用例数不能为空")
    @Min(0)
    private int passCase;

    @NotNull(message = "失败用例数不能为空")
    @Min(0)
    private int failCase;

    @NotNull(message = "错误用例数不能为空")
    @Min(0)
    private int errorCase;

    @NotNull(message = "耗时不能为空")
    @NotBlank(message = "耗时不能为空")
    private String elapsedTime;

    @NotNull(message = "开始时间不能为空")
    @NotBlank(message = "开始时间不能为空")
    private String startTime;

    @NotNull(message = "通过率不能为空")
    @NotBlank(message = "通过率不能为空")
    private String passRate;

    @NotNull(message = "ROM版本不能为空")
    @NotBlank(message = "ROM版本不能为空")
    private String romVersion;

    @NotNull(message = "APK版本不能为空")
    @NotBlank(message = "APK版本不能为空")
    private String apkVersion;

    @NotNull(message = "网络不能为空")
    @NotBlank(message = "网络不能为空")
    private String netWork;

    @NotNull(message = "环境不能为空")
    @NotBlank(message = "环境不能为空")
    private String env;

    private int countCase;
    private String coverage;


    public String getCoverage() {
        return String.format("%.2f", (((float) totalCase / countCase) * 100)) + "%";
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }


    public int getCountCase() {
        return countCase;
    }

    public void setCountCase(int countCase) {
        this.countCase = countCase;
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

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTotalCase() {
        return totalCase;
    }

    public void setTotalCase(int totalCase) {
        this.totalCase = totalCase;
    }

    public int getPassCase() {
        return passCase;
    }

    public void setPassCase(int passCase) {
        this.passCase = passCase;
    }

    public int getFailCase() {
        return failCase;
    }

    public void setFailCase(int failCase) {
        this.failCase = failCase;
    }

    public int getErrorCase() {
        return errorCase;
    }

    public void setErrorCase(int errorCase) {
        this.errorCase = errorCase;
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


    public String getApkVersion() {
        return apkVersion;
    }

    public void setApkVersion(String apkVersion) {
        this.apkVersion = apkVersion;
    }

    public String getNetWork() {
        return netWork;
    }

    public void setNetWork(String netWork) {
        this.netWork = netWork;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }


    public String getRomVersion() {
        return romVersion;
    }

    public void setRomVersion(String romVersion) {
        this.romVersion = romVersion;
    }

}
