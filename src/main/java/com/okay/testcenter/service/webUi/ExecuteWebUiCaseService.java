package com.okay.testcenter.service.webUi;

import com.okay.testcenter.domain.ui.WebCase;

import java.util.List;

public interface ExecuteWebUiCaseService {


    public boolean executeCase(Integer id);

    public boolean executeCaseList(List<WebCase> webCases);
}
