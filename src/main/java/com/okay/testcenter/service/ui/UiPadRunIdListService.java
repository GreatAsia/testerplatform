package com.okay.testcenter.service.ui;


import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.report.UiPadRunIdList;

public interface UiPadRunIdListService {

    public void insertUiPadRunIdList(UiPadRunIdList uiPadRunIdList);

    public void updateUiPadRunIdList(UiPadRunIdList uiPadRunIdList);

    public UiPadRunIdList findUiPadRunIdList(int id);

    public void deleteUiPadById(int id);

    public Integer getLastId();

    public PageInfo findUiPadRunList(int currentPage, int pageSize);
}
