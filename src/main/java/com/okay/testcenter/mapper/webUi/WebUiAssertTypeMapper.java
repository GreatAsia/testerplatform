package com.okay.testcenter.mapper.webUi;

import com.okay.testcenter.domain.ui.AssertType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WebUiAssertTypeMapper {
    public List<AssertType> getAssertTypeList(AssertType assertType);
    public AssertType getAssertTypeById(@Param("id") Integer id);
}
