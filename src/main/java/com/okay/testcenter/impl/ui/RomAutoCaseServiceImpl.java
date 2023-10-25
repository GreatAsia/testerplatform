package com.okay.testcenter.impl.ui;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.ui.PadAutoCase;
import com.okay.testcenter.mapper.ui.RomAutoCaseMapper;
import com.okay.testcenter.service.ui.RomAutoCaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("RomAutoCaseService")
public class RomAutoCaseServiceImpl implements RomAutoCaseService {

    @Resource
    RomAutoCaseMapper romAutoCaseMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCase(PadAutoCase padAutoCase) {
        romAutoCaseMapper.insertCase(padAutoCase);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCase(int id) {
        romAutoCaseMapper.deleteCase(id);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCase(PadAutoCase padAutoCase) {
        romAutoCaseMapper.updateCase(padAutoCase);

    }

    @Override
    public PadAutoCase findCaseById(int id) {
        return romAutoCaseMapper.findCaseById(id);
    }

    @Override
    public List<PadAutoCase> findCaseByName(String name) {
        return romAutoCaseMapper.findCaseByName(name);
    }

    @Override
    public PageInfo findCaseList(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);

        List<PadAutoCase> padAutoCaseList = romAutoCaseMapper.findCaseList();
        PageInfo pageInfo = new PageInfo(padAutoCaseList);

        return pageInfo;
    }
}
