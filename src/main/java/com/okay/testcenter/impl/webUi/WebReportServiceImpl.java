package com.okay.testcenter.impl.webUi;

import com.okay.testcenter.domain.ui.WebReport;
import com.okay.testcenter.domain.ui.WebReportDesc;
import com.okay.testcenter.mapper.webUi.WebReportDescMapper;
import com.okay.testcenter.mapper.webUi.WebReportMapper;
import com.okay.testcenter.service.webUi.WebReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("WebReportService")
public class WebReportServiceImpl implements WebReportService {

    @Resource
    WebReportMapper webReportMapper;
    @Resource
    WebReportDescMapper webReportDescMapper;

    @Override
    public List<WebReport> getWebReportList(WebReport webReport) {
        return webReportMapper.getWebReportList(webReport);
    }

    @Override
    public List<WebReportDesc> getWebReportDescList(WebReportDesc webReportDesc) {
        return webReportDescMapper.getWebReportDescList(webReportDesc);
    }
}
