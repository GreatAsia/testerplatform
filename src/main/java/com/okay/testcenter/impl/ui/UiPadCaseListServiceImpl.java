package com.okay.testcenter.impl.ui;

import com.okay.testcenter.domain.report.UiPadCaseList;
import com.okay.testcenter.mapper.ui.UiPadCaseListMapper;
import com.okay.testcenter.service.ui.UiPadCaseListService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("UiPadCaseListService")
public class UiPadCaseListServiceImpl implements UiPadCaseListService {


    @Resource
    UiPadCaseListMapper uiPadCaseListMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUiPadCaseList(UiPadCaseList uiPadCaseList) {

        uiPadCaseListMapper.insertUiPadCaseList(uiPadCaseList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUiPadCaseList(UiPadCaseList uiPadCaseList) {

        uiPadCaseListMapper.updateUiPadCaseList(uiPadCaseList);

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleterUiPadCaseList(int id) {

        uiPadCaseListMapper.deleterUiPadCaseList(id);

    }

    @Override
    public UiPadCaseList findUiPadCaseListById(int id) {
        return uiPadCaseListMapper.findUiPadCaseListById(id);
    }

    @Override
    public List<UiPadCaseList> findUiPadCaseListByRunId(int id) {
        return uiPadCaseListMapper.findUiPadCaseListByRunId(id);
    }

    @Override
    public List<UiPadCaseList> findUiPadCaseListBySerialno(String serialno) {
        return uiPadCaseListMapper.findUiPadCaseListBySerialno(serialno);
    }

    @Override
    public List<UiPadCaseList> findUiPadCaseListByRunIdAndSerialno(int id, String serialno) {
        return uiPadCaseListMapper.findUiPadCaseListByRunIdAndSerialno(id,serialno);
    }

    @Override
    public List<UiPadCaseList> findUiPadCaseList() {

        return uiPadCaseListMapper.findUiPadCaseList();
    }
}
