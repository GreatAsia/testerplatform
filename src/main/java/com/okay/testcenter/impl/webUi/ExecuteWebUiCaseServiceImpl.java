package com.okay.testcenter.impl.webUi;

import com.okay.testcenter.domain.ui.WebCase;
import com.okay.testcenter.exception.myException.DataException;
import com.okay.testcenter.mapper.webUi.WebCaseMapper;
import com.okay.testcenter.service.webUi.ExecuteWebUiCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("ExecuteWebUiCaseService")
public class ExecuteWebUiCaseServiceImpl implements ExecuteWebUiCaseService {

    @Autowired
    WebUiThreader webUiThreader;


    @Resource
    WebCaseMapper webCaseMapper;

    @Override
    @Transactional(rollbackFor = DataException.class)
    public boolean executeCase(Integer id) {
        WebCase webCase = webCaseMapper.getWebCaseById(id);
        if (webCase == null) {
            return false;
        }
        int upTestStatus = webCaseMapper.updateTestStatusOrScreenshot(webCase.getId(), '1', null);
        if (upTestStatus < 1) {
            throw new DataException();
        }
        webUiThreader.asyncProcess(webCase);

        return true;
    }


    @Override
    @Transactional(rollbackFor = DataException.class)
    public boolean executeCaseList(List<WebCase> webCases) {
        for(WebCase webCase:webCases){
            webCase.setTestStatus('1');
            webCase.setScreenshot(null);
        }
        int upTestStatus = webCaseMapper.updateWebCases(webCases);
        if (upTestStatus < 1) {
            throw new DataException();
        }
        webUiThreader.asyncListProcess(webCases);

        return true;
    }
}
