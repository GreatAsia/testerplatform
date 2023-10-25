package com.okay.testcenter.service.ui;

import com.okay.testcenter.domain.report.UiPadSerialnoList;

import java.util.List;

public interface UiPadSerialnoListService {


    public void insertUiPadSerialnoList(UiPadSerialnoList uiPadSerialnoList);

    public void updateUiPadSerialnoList(UiPadSerialnoList uiPadSerialnoList);

    public void deleterUiPadSerialnoList(int id);

    public UiPadSerialnoList findUiPadSerialnoListById(int id);

    public List<UiPadSerialnoList> findUiPadSerialnoListByRunId(int id);

    public UiPadSerialnoList findUiPadSerialnoListBySerialno(String serialno);

    public List<UiPadSerialnoList> findUiPadSerialnoList();

    public UiPadSerialnoList findUiPadSerialnoListByRunIdAndSerialno(int id, String serialno);


}
