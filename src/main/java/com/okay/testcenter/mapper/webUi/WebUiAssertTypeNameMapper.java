package com.okay.testcenter.mapper.webUi;

import com.okay.testcenter.domain.ui.AssertType;
import com.okay.testcenter.domain.ui.AssertTypeName;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WebUiAssertTypeNameMapper {
    public List<AssertTypeName> getAssertTypeListName(AssertTypeName assertTypeName);

    public AssertTypeName getAssertById(@Param("id") Integer id);
}
