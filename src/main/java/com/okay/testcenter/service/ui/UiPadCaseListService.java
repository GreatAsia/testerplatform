package com.okay.testcenter.service.ui;

import com.okay.testcenter.domain.report.UiPadCaseList;

import java.util.List;

public interface UiPadCaseListService {


    public void insertUiPadCaseList(UiPadCaseList uiPadCaseList);

    public void updateUiPadCaseList(UiPadCaseList uiPadCaseList);

    public void deleterUiPadCaseList(int id);

    public UiPadCaseList findUiPadCaseListById(int id);

    public List<UiPadCaseList> findUiPadCaseListByRunId(int id);

    public List<UiPadCaseList> findUiPadCaseListBySerialno(String serialno);

    public List<UiPadCaseList> findUiPadCaseListByRunIdAndSerialno(int id, String serialno);

    public List<UiPadCaseList> findUiPadCaseList();


}
