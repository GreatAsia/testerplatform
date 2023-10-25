package com.okay.testcenter.impl.ui;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.report.UiPadRunIdList;
import com.okay.testcenter.mapper.ui.UiPadRunIdListMapper;
import com.okay.testcenter.service.ui.UiPadRunIdListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("UiPadRunIdListService")
public class UiPadRunIdListServiceImpl implements UiPadRunIdListService {


    @Resource
    UiPadRunIdListMapper uiPadRunIdListMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUiPadRunIdList(UiPadRunIdList uiPadRunIdList) {

        uiPadRunIdListMapper.insertUiPadRunIdList(uiPadRunIdList);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUiPadRunIdList(UiPadRunIdList uiPadRunIdList) {

        uiPadRunIdListMapper.updateUiPadRunIdList(uiPadRunIdList);

    }

    @Override
    public UiPadRunIdList findUiPadRunIdList(int id) {

        return uiPadRunIdListMapper.findUiPadRunIdList(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUiPadById(int id) {

        uiPadRunIdListMapper.deleteUiPadById(id);
    }

    @Override
    public Integer getLastId() {
        Integer id = uiPadRunIdListMapper.getLastId();
        if (id == null) {
            return 1;
        } else {
            return Integer.parseInt(id + "");
        }

    }

    @Override
    public PageInfo findUiPadRunList(int currentPage, int pageSize) {

        PageHelper.startPage(currentPage, pageSize);
        List<UiPadRunIdList> uiPadRunIdLists = uiPadRunIdListMapper.findUiPadRunList(currentPage, pageSize);
        PageInfo pageInfo = new PageInfo(uiPadRunIdLists);
        return pageInfo;
    }


}
