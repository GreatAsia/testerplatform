package com.okay.testcenter.service.ui;

import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.ui.PadAutoCase;

import java.util.List;


public interface RomAutoCaseService {

    public void insertCase(PadAutoCase padAutoCase);

    public void deleteCase(int id);

    public void updateCase(PadAutoCase padAutoCase);

    public PadAutoCase findCaseById(int id);

    public List<PadAutoCase> findCaseByName(String name);

    public PageInfo findCaseList(int currentPage, int pageSize);


}
