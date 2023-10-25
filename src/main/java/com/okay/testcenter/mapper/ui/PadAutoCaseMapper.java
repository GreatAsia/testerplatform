package com.okay.testcenter.mapper.ui;

import com.okay.testcenter.domain.ui.PadAutoCase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PadAutoCaseMapper {


    public void insertCase(PadAutoCase padAutoCase);

    public void deleteCase(int id);

    public void updateCase(PadAutoCase padAutoCase);

    public PadAutoCase findCaseById(int id);

    public List<PadAutoCase> findCaseByName(String name);

    public List<PadAutoCase> findCaseList();

}
