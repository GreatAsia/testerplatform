package com.okay.testcenter.impl.webUi;

import com.okay.testcenter.domain.ui.WebCase;
import com.okay.testcenter.domain.ui.WebReport;
import com.okay.testcenter.domain.ui.WebReportDesc;
import com.okay.testcenter.mapper.webUi.WebCaseMapper;
import com.okay.testcenter.mapper.webUi.WebReportDescMapper;
import com.okay.testcenter.mapper.webUi.WebReportMapper;
import com.okay.testcenter.service.webUi.WebUIService;
import com.okay.testcenter.tools.GetTime;
import com.okay.testcenter.tools.webUi.SeleniumApiUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author XieYangYang
 * @date 2020/6/5 16:45
 */
@Log4j
@Component
public class WebUiThreader {

    @Autowired
    WebUIService webUIService;

    @Autowired
    SeleniumApiUtil seleniumApiUtil;

    @Resource
    WebReportDescMapper webReportDescMapper;

    @Resource
    WebReportMapper webReportMapper;

    @Resource
    WebCaseMapper webCaseMapper;

    /**
     * 单条用例
     *
     * @param myWebCase
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void asyncProcess(WebCase myWebCase) {
        try {
            WebReport webReport = new WebReport();
            WebReportDesc webReportDesc = new WebReportDesc();

            webReport.setProjectIds(myWebCase.getPlatformId() + "");
            webReport.setProjectNames(myWebCase.getWebPlatform().getPlatformName());

            webReport.setAllCaseCount(1);
            //开始时间
            webReport.setStartTime(GetTime.getTimeFormat());

            webReportDesc.setStartTime(GetTime.getTimeFormat());
            webUIService.seleniumExecuteByWebCase(myWebCase, webReportDesc);
            webReportDesc.setEndTime(GetTime.getTimeFormat());
            //结束时间
            webReport.setEndTime(GetTime.getTimeFormat());

            //设置失败条数
            if (webReportDesc.getStatus() == '3') {
                webReport.setFailCaseCount(1);
            }
            if (webReportDesc.getStatus() == '2') {
                webReport.setFailCaseCount(0);
            }

            webReportMapper.addWebReport(webReport);
            webReportDesc.setWebReportId(webReport.getId());
            webReportDescMapper.addWebReportDesc(webReportDesc);

            SeleniumApiUtil.SingletonCookie singletonCookie = seleniumApiUtil.new SingletonCookie();
            singletonCookie.cleanCookies(myWebCase.getWebPlatform().getPlatformName() + "_" + myWebCase.getPlatformId());
        } catch (Exception e) {
            myWebCase.setTestStatus('3');
            int isInsert = webCaseMapper.addWebCase(myWebCase);
            log.error(e);
            throw new RuntimeException();
        }

    }

    /**
     * 多条用例
     *
     * @param myWebCases
     */
    @Async
    public void asyncListProcess(List<WebCase> myWebCases) {
        excuteCaseList(myWebCases);
    }

    @Transactional(rollbackFor = Exception.class)
    public void excuteCaseList(List<WebCase> myWebCases) {
        WebReport webReport = new WebReport();
        List<WebReportDesc> webReportDescList = new ArrayList<WebReportDesc>();


        webReport.setAllCaseCount(myWebCases.size());
        int errCount = 0;
        webReport.setStartTime(GetTime.getTimeFormat());
        String platformNames = "";
        String platformIds = "";
        List<String> cookieNames = new ArrayList<>();
        for (WebCase myWebCase : myWebCases) {
            if (!platformNames.contains(myWebCase.getWebPlatform().getPlatformName())) {
                platformNames = platformNames.equals("") ? myWebCase.getWebPlatform().getPlatformName() : "," + myWebCase.getWebPlatform().getPlatformName();
            }
            if (!platformIds.contains(String.valueOf(myWebCase.getPlatformId()))) {
                platformIds = platformIds.equals("") ? myWebCase.getPlatformId() + "" : "," + myWebCase.getPlatformId();
            }

            WebReportDesc webReportDesc = new WebReportDesc();
            webReportDesc.setStartTime(GetTime.getTimeFormat());
            webUIService.seleniumExecuteByWebCase(myWebCase, webReportDesc);
            webReportDesc.setEndTime(GetTime.getTimeFormat());
            //设置失败条数
            if (webReportDesc.getStatus() == '3') {
                errCount = errCount + 1;
            }

            cookieNames.add(myWebCase.getWebPlatform().getPlatformName() + "_" + myWebCase.getPlatformId());

            webReportDescList.add(webReportDesc);
        }
        webReport.setProjectNames(platformNames);
        webReport.setProjectIds(platformIds);
        webReport.setEndTime(GetTime.getTimeFormat());
        webReport.setFailCaseCount(errCount);
        int resAddWebReport = webReportMapper.addWebReport(webReport);
        if (resAddWebReport < 1) {
            throw new RuntimeException();
        }
        //设置所属报告id
        for (WebReportDesc webReportDesc : webReportDescList) {
            webReportDesc.setWebReportId(webReport.getId());
        }
        int resAddWebReportDescList = webReportDescMapper.addWebReportDescList(webReportDescList);
        if (resAddWebReportDescList < 1) {
            throw new RuntimeException();
        }
        SeleniumApiUtil.SingletonCookie singletonCookie = seleniumApiUtil.new SingletonCookie();
        for (String cookieName : cookieNames) {
            singletonCookie.cleanCookies(cookieName);
        }

    }
}
