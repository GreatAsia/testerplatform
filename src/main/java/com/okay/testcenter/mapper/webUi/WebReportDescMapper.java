package com.okay.testcenter.mapper.webUi;

import com.okay.testcenter.domain.ui.WebReportDesc;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WebReportDescMapper {
    public int addWebReportDesc(WebReportDesc webReportDesc);

    public int addWebReportDescList(List<WebReportDesc> webReportDesc);

    public List<WebReportDesc> getWebReportDescList(WebReportDesc webReportDesc);
}
