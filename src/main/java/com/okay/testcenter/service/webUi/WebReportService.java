package com.okay.testcenter.service.webUi;

import com.okay.testcenter.domain.ui.WebReport;
import com.okay.testcenter.domain.ui.WebReportDesc;

import java.util.List;

public interface WebReportService {
    public List<WebReport> getWebReportList(WebReport webReport);

    public List<WebReportDesc> getWebReportDescList(WebReportDesc webReportDesc);
}
