package com.okay.testcenter.mapper.ui;

import com.okay.testcenter.domain.report.UiPadCaseList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UiPadCaseListMapper {

    public void insertUiPadCaseList(UiPadCaseList uiPadCaseList);

    public void updateUiPadCaseList(UiPadCaseList uiPadCaseList);

    public void deleterUiPadCaseList(int id);

    public UiPadCaseList findUiPadCaseListById(int id);

    public List<UiPadCaseList> findUiPadCaseListByRunId(int id);

    public List<UiPadCaseList> findUiPadCaseListBySerialno(String serialno);

    public List<UiPadCaseList> findUiPadCaseList();

    public List<UiPadCaseList> findUiPadCaseListByRunIdAndSerialno(int id, String serialno);
}
