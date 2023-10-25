package com.okay.testcenter.mapper.webUi;

import com.okay.testcenter.domain.ui.WebReport;
import com.okay.testcenter.domain.ui.WebReportDesc;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WebReportMapper {
    public int addWebReport(WebReport webReport);
    public List<WebReport> getWebReportList(WebReport webReport);
    public WebReport getNewWebReport();
}
