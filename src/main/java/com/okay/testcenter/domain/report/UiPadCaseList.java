package com.okay.testcenter.domain.report;

import com.okay.testcenter.domain.bean.Base;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 对应 ui_pad_caselist 表
 */

public class UiPadCaseList  extends Base {

    @NotNull
    private int id;

    @NotNull(message = "运行ID不能为空")
    @Min(1)
    private int runId;

    @NotNull(message = "序列号不能为空")
    @NotBlank(message = "序列号不能为空")
    private String serialno;

    @NotNull(message = "用例名不能为空")
    @NotBlank(message = "用例名不能为空")
    private String caseName;

    @NotNull(message = "用例说明不能为空")
    @NotBlank(message = "用例说明不能为空")
    private String caseDesc;

    @NotNull(message = "结果不能为空")
    @NotBlank(message = "结果不能为空")
    private String result;

    @NotNull(message = "耗时不能为空")
    @NotBlank(message = "耗时不能为空")
    private String elapsedTime;

    @NotNull(message = "开始时间不能为空")
    @NotBlank(message = "开始时间不能为空")
    private String startTime;


    private String errorLog;
    private String caseResult;

    @NotNull(message = "所属模块不能为空")
    @NotBlank(message = "所属模块不能为空")
    private String caseModule;


    private String testLogPath;
    private String picturePath;
    private List<String> picturePathList = new ArrayList<>();
    private List<BufferedImage> pictureContent = new ArrayList<>();


    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public List<String> getPicturePathList() {
        return picturePathList;
    }

    public void setPicturePathList(List<String> picturePathList) {
        this.picturePathList = picturePathList;
    }

    public String getTestLogPath() {
        return testLogPath;
    }

    public void setTestLogPath(String testLogPath) {
        this.testLogPath = testLogPath;
    }








    public List<BufferedImage> getPictureContent() {
        return pictureContent;
    }

    public void setPictureContent(List<BufferedImage> pictureContent) {
        this.pictureContent = pictureContent;
    }


    public String getCaseModule() {
        return caseModule;
    }

    public void setCaseModule(String caseModule) {
        this.caseModule = caseModule;
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

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }


    public String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCaseResult() {
        return caseResult;
    }

    public void setCaseResult(String caseResult) {
        this.caseResult = caseResult;
    }


}
