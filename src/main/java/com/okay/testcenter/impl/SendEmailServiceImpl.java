package com.okay.testcenter.impl;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.dubbo.DubboCaseHistory;
import com.okay.testcenter.domain.dubbo.DubboTestHistory;
import com.okay.testcenter.domain.report.*;
import com.okay.testcenter.domain.ui.WebReport;
import com.okay.testcenter.domain.ui.WebReportDesc;
import com.okay.testcenter.mapper.webUi.WebReportDescMapper;
import com.okay.testcenter.mapper.webUi.WebReportMapper;
import com.okay.testcenter.service.SendEmailService;
import com.okay.testcenter.service.dubbo.DubboInterfaceHistoryService;
import com.okay.testcenter.service.dubbo.DubboTestHistoryService;
import com.okay.testcenter.service.middle.MiddleRequestHisoryService;
import com.okay.testcenter.service.middle.MiddleTestHistoryService;
import com.okay.testcenter.service.ui.UiPadCaseListService;
import com.okay.testcenter.service.ui.UiPadRunIdListService;
import com.okay.testcenter.service.ui.UiPadSerialnoListService;
import com.okay.testcenter.tools.MailInfo;
import com.okay.testcenter.tools.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhou
 * @date 2019/11/3
 */


@Service("SendEmailService")
public class SendEmailServiceImpl implements SendEmailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${logging.path}")
    private String logPath;

    private int emailWidth = 800;
    private String projectName = "监控";

    @Value("${server.root.url}")
    private String platformUrl;

    @Resource
    MiddleTestHistoryService middleTestHistoryService;
    @Resource
    MiddleRequestHisoryService middleRequestHisoryService;
    @Resource
    DubboTestHistoryService dubboTestHistoryService;
    @Resource
    DubboInterfaceHistoryService dubboInterfaceHistoryService;

    @Resource
    UiPadRunIdListService uiPadRunIdListService;
    @Resource
    UiPadSerialnoListService uiPadSerialnoListService;
    @Resource
    UiPadCaseListService uiPadCaseListService;

    @Resource
    WebReportDescMapper webReportDescMapper;

    @Resource
    WebReportMapper webReportMapper;
    @Value("${server.root.url}")
    private String platformHost;
    private final String errorUrl = platformHost + "/middle/report/detail/";

    private String commonHeader = "<!DOCTYPE html>    \n" +
            "<html>    \n" +
            "<head>\n" +
            "\n" +
            "<title>Test Report</title>\n" +
            "</head> " +
            "<body >" +
            "<table width=" + emailWidth + "cellspacing=\"0\" cellpadding=\"0\" border=\"0\" name=\"bmeMainBody\" ";

    private String passHeader = commonHeader + "bgcolor=\"#C7EDCC\"> ";


    private String failHeader = commonHeader + "bgcolor=\"#F0DAD2\"> ";


    private String tail = "</table></body></html>" +
            "<br>";

    private String failCase = failHeader + "<caption class=\"caption\"></h1><strong>FAIL用例</strong></h1></caption>";
    private String passCase = passHeader + "<caption class=\"caption\"></h1><strong>PASS用例</strong></h1></caption>";


    @Override
    public Boolean sendEmail(JSONObject request) {

//        String errorLogPath = logPath + "/server-error." + GetTime.getYmd() + ".log";
        String sendTo = request.getString("sendTo");
        String title = request.getString("title");
        //middle、app、dubbo
        String item = request.getString("item");
        int historyId = request.getInteger("historyId");

        logger.info("[收件人]==" + sendTo);
//        logger.info("[附件]==" + errorLogPath);
        logger.info("[标题]==" + title);
        logger.info("[historyId]==" + historyId);


        MailInfo mailInfo = new MailInfo();
        List<String> toList = new ArrayList<>();
        String[] sendToArray = sendTo.split(";");
        if (sendToArray.length > 0) {
            for (int i = 0; i < sendToArray.length; i++) {

                toList.add(sendToArray[i]);
            }

        } else {
            logger.error("收件人错误,请检查==" + sendTo);
        }
        //添加附件
//        EmailAttachment att = new EmailAttachment();
//        att.setPath(errorLogPath);
//        att.setName("errorlog.txt");
//        List<EmailAttachment> atts = new ArrayList<EmailAttachment>();
//        atts.add(att);
//        mailInfo.setAttachments(atts);
        //收件人
        mailInfo.setToAddress(toList);
        if (historyId == 0) {
            mailInfo.setContent("执行监控发生异常，请检查日志");
        } else {

            mailInfo.setContent(getEmailContent(item, historyId));
        }
        mailInfo.setSubject(projectName + "_" + title);
        Boolean result = MailUtil.sendEmail(mailInfo);

        return result;

    }

    @Value("${server.root.url}")
    String url;

    @Override
    public Boolean sendEmailWebUi(JSONObject requestJson) {
        System.out.println(url);
        String webReportId = requestJson.getString("webReportId");

        WebReport webReport=new WebReport();
        webReport.setId(Integer.valueOf(webReportId));
        webReport=webReportMapper.getWebReportList(webReport).get(0);

        WebReportDesc webReportDesc = new WebReportDesc();
        webReportDesc.setWebReportId(Integer.valueOf(webReportId));
        List<WebReportDesc> webReportDescs = webReportDescMapper.getWebReportDescList(webReportDesc);


        List<String> toList = new ArrayList<>();
        String listAddresses = requestJson.getString("sendTo");
        String[] sendToArray = listAddresses.split(";");
        if (sendToArray.length > 0) {
            for (int i = 0; i < sendToArray.length; i++) {

                toList.add(sendToArray[i]);
            }

        } else {
            logger.error("收件人错误,请检查==" + listAddresses);
        }
        String emailContent = setWebUiemailContent(url, webReport, webReportDescs);

        MailInfo mailInfo = new MailInfo();
        mailInfo.setSubject(webReport.getProjectNames() + "_前端自动化测试报告");
        mailInfo.setContent(emailContent);
        mailInfo.setToAddress(toList);
        Boolean result = MailUtil.sendEmail(mailInfo);
        return result;
    }

    /**
     * 获取请求信息
     *
     * @param request
     * @return
     */
    private String getBodyData(ServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
        } finally {
        }
        return data.toString();
    }


    private String setWebUiemailContent(String url, WebReport webReport, List<WebReportDesc> webReportDescs) {

        String stringWebReportDescs = "";
        for (WebReportDesc webReportDesc : webReportDescs) {
            String status = webReportDesc.getStatus() == '3' ? "<span style=\"color: red\">失败</span>" : "成功";
            String pic = (webReportDesc.getShotPic() == null) ? "<img src=\"" + url  + "/image/unexecutedCase.jpg\" style=\"width: 280px; height: 180px\">" : "<img src=\"" + url + webReportDesc.getShotPic() + "\" style=\"width: 280px; height: 180px\">";
            String errormsg=webReportDesc.getErrorMsg()==null?"":webReportDesc.getErrorMsg();
            stringWebReportDescs += "<tr>\n" +
                    "            <td style=\"height: 200px\">" + webReportDesc.getId() + "</td>\n" +
                    "            <td style=\"height: 200px\">" + webReportDesc.getCaseName() + "</td>\n" +
                    "            <td style=\"height: 200px\">" + webReportDesc.getUrl() + "</td>\n" +
                    "            <td style=\"height: 200px\">" + webReportDesc.getStartTime() + "</td>\n" +
                    "            <td style=\"height: 200px\">" + webReportDesc.getEndTime() + "</td>\n" +
                    "            <td style=\"height: 200px\">" + pic + "</td>\n" +
                    "            <td style=\"height: 200px\">" + status + "</td>\n" +
                    "            <td style=\"height: 200px\">" + errormsg + "</td>\n" +
                    "        </tr>";
        }


        String status = webReport.getFailCaseCount() > 0 ? "<span style=\"color: red\">失败</span>" : "成功";
        String emailContent = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "    <title></title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div style=\"width:1430px;text-align: center;\"><span style=\"text-align: center;font-size: 20px;\">"+webReport.getProjectNames()+"webUi自动化测试报告</span></div>\n" +
                "<table border=\"1px solid #ccc\"  cellpadding=\"1px\" width=\"\"  cellspacing=\"1px\" style=\"border-collapse:collapse; font-family: 微软雅黑; font-size: 15px;border: 1px solid #ccc; min-width:300px\">\n" +
                "    <tbody>\n" +
                "        <tr>\n" +
                "            <td style=\"background: rgb(222,226,230)\">项目名称:" + webReport.getProjectNames() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background: rgb(222,226,230)\">开始时间:" + webReport.getStartTime() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background: rgb(222,226,230)\">结束时间:" + webReport.getEndTime() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background: rgb(222,226,230)\">执行状态:" + status + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background: rgb(222,226,230)\">失败案例条数:" + webReport.getFailCaseCount() + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background: rgb(222,226,230)\">全部案例条数:" + webReport.getAllCaseCount() + "</td>\n" +
                "        </tr>\n" +
                "    </tbody>\n" +
                "</table>\n" +
                "<div style=\"width:1430px;\">&nbsp;</div>\n" +
                "<table border=\"1px solid #ccc\"  cellpadding=\"1px\" width=\"\"  cellspacing=\"1px\" style=\"border-collapse:collapse; font-family: 微软雅黑; font-size: 13px;border: 1px solid #ccc; min-width:1430px\">\n" +
                "    <thead  style=\"background: rgb(222,226,230)\">\n" +
                "        <th style=\"width: 50px ;background: rgb(222,226,230)\">ID</th>\n" +
                "        <th style=\"width: 200px ;background: rgb(222,226,230)\">用例名称</th>\n" +
                "        <th style=\"width: 250px ;background: rgb(222,226,230)\">url</th>\n" +
                "        <th style=\"width: 150px ;background: rgb(222,226,230)\">开始时间</th>\n" +
                "        <th style=\"width: 150px ;background: rgb(222,226,230)\">结束时间</th>\n" +
                "        <th style=\"width: 300px ;background: rgb(222,226,230)\">截图</th>\n" +
                "        <th style=\"width: 80px ;background: rgb(222,226,230)\">状态</th>\n" +
                "        <th style=\"width: 250px ;background: rgb(222,226,230)\">错误信息</th>\n" +
                "    </thead>\n" +
                "    <tbody style=\"text-align: center\">\n" +
                stringWebReportDescs +
                "    </tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";
        return emailContent;
    }


    private String getEmailContent(String item, int id) {
        String content = "";
        switch (item) {

            case "middle":
                content = getMiddleContent(id);
                break;
            case "app":
                content = getAppContent(id);
                break;
            case "dubbo":
                content = getDubboContent(id);
                break;
            default:
                logger.error("类型名称错误==" + item);
        }

        return content;

    }


    /**
     * 报告汇总中间层
     *
     * @param history
     * @return
     */
    private String sunmaryInfo(MiddleTestHistory history) {

        String sunmary = passHeader +
                "<caption class=\"caption\"></h1><strong>报告汇总</strong></h1></caption>\n" +
                "<tr><td style=\"font-size:14px\">[运行ID]:" + history.getId() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[项目名称]:" + history.getProjectName() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[开始时间]:" + history.getStartTime() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[结束时间]:" + history.getEndTime() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[测试平台地址]:" + platformUrl + "/middle/report/detail/" + history.getId() + " </td></tr>" +
                historyResult(history.getResult()) +
                "<tr><td style=\"font-size:14px\">[用例总数]:" + history.getTotalCase() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[通过数量]:" + history.getPassCase() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\";color:\"#ac2925\";>[失败数量]:" + history.getFailCase() + "</td></tr>" +
                tail;
        return sunmary;
    }

    /**
     * 中间层响应内容
     *
     * @param request
     * @return
     */
    private String bodyInfo(MiddleRequestHistory request) {

        String content = "<tr><td>[用例名称]:" + request.getCaseName() + "</td></tr>" +
                "<tr><td style=\"word-wrap: break-word;\" >[请求地址]:" + request.getUrl() + "</td></tr>" +
                "<tr><td style=\"word-wrap: break-word;\" >[requestId]:" + request.getRequestId() + "</td></tr>" +
                "<tr><td style=\"word-wrap: break-word;\" >[HTTP状态码]:" + request.getResponseCode() + "</td></tr>" +
                "<tr><td style=\"word-wrap: break-word;\" >[耗时(ms)]:" + request.getElapsed_time() + "</td></tr>" +
                "<tr><td style=\"word-wrap: break-word;\" >[请求数据]:<xmp style=\"white-space:normal;\">" + request.getRequestData() + "</xmp></td></tr>" +
                "<tr><td style=\"word-wrap: break-word;\" >[响应数据]:<xmp style=\"white-space:normal;\">" + request.getResponseContent() + "</xmp></td></tr>" +
                "</table><br>";

        if ("FAIL".equals(request.getResult())) {
            content = "<table width=" + emailWidth + "cellspacing=\"0\" cellpadding=\"0\" border=\"0\"  style=\"background-color: #F0DAD2;table-layout:fixed\">" + content;
        } else {
            content = "<table width=" + emailWidth + "cellspacing=\"0\" cellpadding=\"0\" border=\"0\"  style=\"background-color: #C7EDCC;table-layout:fixed\">" + content;
        }
        return content;
    }


    /**
     * 获取中间层内容
     *
     * @param id 用例ID
     * @return
     */
    private String getMiddleContent(int id) {

        MiddleTestHistory history = middleTestHistoryService.findMiddleTestHistoryById(id);
        List<MiddleRequestHistory> requestList = middleRequestHisoryService.findHistoryByHistoryId(id);
        projectName = history.getProjectName();
        String contentHeader = sunmaryInfo(history);

        StringBuffer errorContentBody = new StringBuffer();
        errorContentBody.append(failCase);
        StringBuffer passContentBody = new StringBuffer();
        passContentBody.append(passCase);
        //添加一个变量,判断是否有失败用例
        int failMiddleCaseCount = 0;
        for (MiddleRequestHistory request : requestList) {

            if ("FAIL".equals(request.getResult())) {
                errorContentBody.append(bodyInfo(request));
                failMiddleCaseCount++;
            } else {
                passContentBody.append(bodyInfo(request));
            }
        }
        passContentBody.append(tail);
        errorContentBody.append(tail);

        if (failMiddleCaseCount > 0) {
            return contentHeader + errorContentBody ;
        }
        return contentHeader + errorContentBody;

    }


    /**
     * 报告汇总后端
     *
     * @param history
     * @return
     */
    private String dubboSunmaryInfo(DubboTestHistory history) {

        String sunmary = passHeader +
                "<tr><td style=\"font-size:14px\">[运行ID]:" + history.getId() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[服务名称]:" + history.getModelName() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[开始时间]:" + history.getStartTime() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[结束时间]:" + history.getEndTime() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[测试平台地址]:" + platformUrl + "</td></tr>" +
                historyResult(history.getResult()) +
                "<tr><td style=\"font-size:14px\">[用例总数]:" + history.getTotalCase() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[通过数量]:" + history.getPassCase() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\";color:\"#ac2925\">[失败数量]:" + history.getFailCase() + "</td></tr>" +
                tail;
        return sunmary;
    }

    /**
     * 后端响应内容
     *
     * @param request
     * @return
     */
    private String dubboBodyInfo(DubboCaseHistory request) {

        String content = "<tr><td style=\"word-wrap: break-word;\" >[请求数据]:<xmp style=\"white-space:normal;\">" + request.getRequestData() + "</xmp></td></tr>" +
                "<tr><td style=\"word-wrap: break-word;\" >[响应数据]:<xmp style=\"white-space:normal;\">" + request.getResponseContent() + "</xmp></td></tr>" +
                "</table><br>";

        if ("FAIL".equals(request.getResult())) {
            content = "<table width=" + emailWidth + "cellspacing=\"0\" cellpadding=\"0\" border=\"1\"  style=\"background-color: #FF6347;table-layout:fixed\">" + content;
        } else {
            content = "<table width=" + emailWidth + "cellspacing=\"0\" cellpadding=\"0\" border=\"1\"  style=\"background-color: #3CB371;table-layout:fixed\">" + content;
        }
        return content;
    }


    //获取dubbo端报告内容
    private String getDubboContent(int id) {

        DubboTestHistory history = dubboTestHistoryService.findTestHistoryById(id);
        List<DubboCaseHistory> requestList = dubboInterfaceHistoryService.findHistoryByHistoryId(id);
        projectName = history.getModelName();
        String contentHeader = dubboSunmaryInfo(history);
        StringBuffer errorContentBody = new StringBuffer();
        errorContentBody.append(failCase);
        StringBuffer passContentBody = new StringBuffer();
        passContentBody.append(passCase);
        //添加一个变量,判断是否有失败用例
        int failDubboCaseCount = 0;

        for (DubboCaseHistory request : requestList) {
            if ("FAIL".equals(request.getResult())) {
                errorContentBody.append(dubboBodyInfo(request));
                failDubboCaseCount++;
            } else {
                passContentBody.append(dubboBodyInfo(request));

            }
        }
        passContentBody.append(tail);
        errorContentBody.append(tail);

        if (failDubboCaseCount > 0) {
            return contentHeader + errorContentBody + passContentBody;
        }
        return contentHeader + passContentBody;

    }


    //获取APP端报告内容
    private String getAppContent(int id) {

        UiPadRunIdList uiPadRunIdList = uiPadRunIdListService.findUiPadRunIdList(id);
        List<UiPadCaseList> uiPadCaseLists = uiPadCaseListService.findUiPadCaseListByRunId(id);

        int pass = 0;
        int fail = 0;
        int x = 0;
        for (UiPadCaseList uiPadCaseList1 : uiPadCaseLists) {

            uiPadCaseLists.get(x).setId(x + 1);
            if ("true".equals(uiPadCaseList1.getCaseResult())) {
                pass++;
            } else {
                fail++;
            }
            x++;
        }
        //@TODO setCountCase需要修改
        StringBuilder romVersionBuilder = new StringBuilder();
        StringBuilder apkVersionBuilder = new StringBuilder();
        StringBuilder envBuilder = new StringBuilder();
        StringBuilder netWorkBuilder = new StringBuilder();
        StringBuilder versionBuilder = new StringBuilder();

        List<UiPadSerialnoList> uiPadSerialnoLists = uiPadSerialnoListService.findUiPadSerialnoListByRunId(id);
        int i = 0;
        for (UiPadSerialnoList uiPadSerialnoList : uiPadSerialnoLists) {

            if (i == 0) {
                romVersionBuilder.append(uiPadSerialnoList.getRomVersion());
                apkVersionBuilder.append(uiPadSerialnoList.getApkVersion());
                envBuilder.append(uiPadSerialnoList.getEnv());
                netWorkBuilder.append(uiPadSerialnoList.getNetWork());
                versionBuilder.append(uiPadSerialnoList.getVersion());
            }

            if (romVersionBuilder.indexOf(uiPadSerialnoList.getRomVersion()) == -1) {
                romVersionBuilder.append("/" + uiPadSerialnoList.getRomVersion());
            }

            if (apkVersionBuilder.indexOf(uiPadSerialnoList.getApkVersion()) == -1) {
                apkVersionBuilder.append("/").append(uiPadSerialnoList.getApkVersion());
            }
            if (envBuilder.indexOf(uiPadSerialnoList.getEnv()) == -1) {
                envBuilder.append("/").append(uiPadSerialnoList.getEnv());
            }
            if (netWorkBuilder.indexOf(uiPadSerialnoList.getNetWork()) == -1) {
                netWorkBuilder.append("/").append(uiPadSerialnoList.getNetWork());
            }

            if (versionBuilder.indexOf(uiPadSerialnoList.getVersion()) == -1) {
                versionBuilder.append("/").append(uiPadSerialnoList.getVersion());
            }

            i++;
        }

        // 邮件正文内容
        String testEnv = "<table width=" + emailWidth + " cellspacing=\"0\" cellpadding=\"0\" border=\"1\"  style=\"background-color: #a6e9d7\">" +
                "<tr style=\"font-size:16px\"><strong>测试环境</strong></tr>" +
                "<tr><td style=\"font-size:14px\">[ROM版本]:" + romVersionBuilder.toString() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[APK版本]:" + apkVersionBuilder.toString() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[覆盖机型]:" + versionBuilder.toString() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[机器数量]:" + uiPadRunIdList.getTotalDevice() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[网络]:" + netWorkBuilder.toString() + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[环境]:" + envBuilder.toString() + "</td></tr>" +
                "</table>";

        String testDetail = "<table width=" + emailWidth + "cellspacing=\"0\" cellpadding=\"0\" border=\"1\" style=\"background-color: #a6e9d7\">" +
                "<tr style=\"font-size:16px\"><strong>测试详情</strong></tr>" +
                "<tr><td style=\"font-size:14px\">[成功用例数]:" + pass + "</td></tr>" +
                "<tr><td style=\"font-size:14px\";color:\"#ac2925\";>[失败用例数]:" + fail + "&nbsp&nbsp&nbsp&nbsp" + "[通过率]" + String.format("%.2f", (((float) pass / (pass + fail)) * 100)) + "%" + "</td></tr>" +
                "<tr><td style=\"font-size:14px\">[运行用例数]:" + (pass + fail) + "</div>" +
                "<tr><td style=\"font-size:14px\">[用例总数]:" + 35 + "&nbsp&nbsp&nbsp&nbsp" + "[覆盖率]" + String.format("%.2f", (((float) (pass + fail) / 35) * 100)) + "%" + "</td></tr>" +
                "</table>";


        StringBuffer caseBody = new StringBuffer();
        caseBody.append("<table width=" + emailWidth + " cellspacing=\"0\" cellpadding=\"0\" border=\"1\" style=\"background-color: #a6e9d7\" >"
                + "<tr style=\"font-size:16px\"><strong>用例数据</strong></tr>" +
                "<thead><tr>" +
                "<th>用例ID</th>" +
                "<th>用例名称</th>" +
                "<th>所属模块</th>" +
                "<th>测试结果</th>" +
                "</tr></thead>" +
                "<tbody>");

        for (UiPadCaseList list : uiPadCaseLists) {

            caseBody.append("<tr><td align=\"center\"  style=\"font-size:14px\">" + list.getId() + "</td>" +
                    "<td align=\"center\"  style=\"font-size:14px\">" + list.getCaseDesc() + "</td>" +
                    "<td align=\"center\" style=\"font-size:14px\">" + list.getCaseModule() + "</td>" +
                    "<td align=\"center\" style=\"font-size:14px\">" + list.getCaseResult() + "</td></tr>");

        }
        caseBody.append("</tbody></table>");

        return passHeader + testEnv + testDetail + caseBody + tail;


    }


    private String historyResult(String isPass) {
        String result = "";
        if ("PASS".equals(isPass)) {

            result = "<tr><td style=\"font-size:14px\">[测试结果]:Pass</td></tr>";
        } else if ("FAIL".equals(isPass)) {

            result = "<tr><td style=\"font-size:14px\">[测试结果]:<b style=\"color: #dc3545; \">Fail</b></td></tr>";
        }
        return result;

    }


}
