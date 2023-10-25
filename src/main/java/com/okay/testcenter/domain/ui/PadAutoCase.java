package com.okay.testcenter.domain.ui;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * pad自动化case实体类
 *
 */
public class PadAutoCase {

    @NotNull
    private int id;

    @NotNull(message = "文件名称不能为空")
    @NotBlank(message = "文件名称不能为空")
    private String caseName;

    @NotNull(message = "用例详情不能为空")
    @NotBlank(message = "用例详情不能为空")
    private String caseDesc;

    @NotNull(message = "用例名称不能为空")
    @NotBlank(message = "用例名称不能为空")
    private String caseContent;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public String getCaseContent() {
        return caseContent;
    }

    public void setCaseContent(String caseContent) {
        this.caseContent = caseContent;
    }
}
