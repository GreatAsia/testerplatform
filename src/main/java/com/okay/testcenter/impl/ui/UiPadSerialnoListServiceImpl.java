package com.okay.testcenter.impl.ui;


import com.okay.testcenter.domain.report.UiPadSerialnoList;
import com.okay.testcenter.mapper.ui.UiPadSerialnoListMapper;
import com.okay.testcenter.service.ui.UiPadSerialnoListService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("UiPadSerialnoListService")
public class UiPadSerialnoListServiceImpl implements UiPadSerialnoListService {

    @Resource
    UiPadSerialnoListMapper uiPadSerialnoListMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUiPadSerialnoList(UiPadSerialnoList uiPadSerialnoList) {

        uiPadSerialnoListMapper.insertUiPadSerialnoList(uiPadSerialnoList);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUiPadSerialnoList(UiPadSerialnoList uiPadSerialnoList) {

        uiPadSerialnoListMapper.updateUiPadSerialnoList(uiPadSerialnoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleterUiPadSerialnoList(int id) {

        uiPadSerialnoListMapper.deleterUiPadSerialnoList(id);
    }

    @Override
    public UiPadSerialnoList findUiPadSerialnoListById(int id) {

        return uiPadSerialnoListMapper.findUiPadSerialnoListById(id);
    }

    @Override
    public List<UiPadSerialnoList> findUiPadSerialnoListByRunId(int id) {

        return uiPadSerialnoListMapper.findUiPadSerialnoListByRunId(id);
    }

    @Override
    public UiPadSerialnoList findUiPadSerialnoListBySerialno(String serialno) {

        return uiPadSerialnoListMapper.findUiPadSerialnoListBySerialno(serialno);
    }

    @Override
    public List<UiPadSerialnoList> findUiPadSerialnoList() {

        return uiPadSerialnoListMapper.findUiPadSerialnoList();
    }

    @Override
    public UiPadSerialnoList findUiPadSerialnoListByRunIdAndSerialno(int id, String serialno) {
        return uiPadSerialnoListMapper.findUiPadSerialnoListByRunIdAndSerialno(id, serialno);
    }


}
