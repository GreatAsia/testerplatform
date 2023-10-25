package com.okay.testcenter.mapper.ui;

import com.okay.testcenter.domain.report.UiPadRunIdList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UiPadRunIdListMapper {

    public void insertUiPadRunIdList(UiPadRunIdList uiPadRunIdList);

    public void updateUiPadRunIdList(UiPadRunIdList uiPadRunIdList);

    public UiPadRunIdList findUiPadRunIdList(int id);

    public void deleteUiPadById(int id);

    public Integer getLastId();

    public List<UiPadRunIdList> findUiPadRunList(int currentPage, int pageSize);


}
