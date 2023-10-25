package com.okay.testcenter.impl.webUi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.common.enumFile.webUi.TestOperation;
import com.okay.testcenter.common.enumFile.webUi.TestStep;
import com.okay.testcenter.domain.ui.*;
import com.okay.testcenter.exception.myException.*;
import com.okay.testcenter.mapper.webUi.*;
import com.okay.testcenter.service.webUi.WebUIService;
import com.okay.testcenter.tools.webUi.SeleniumApiUtil;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Log4j
@Service("WebUIService")
public class WebUIServiceImpl implements WebUIService {


    @Autowired
    SeleniumApiUtil seleniumApiUtil;

    @Resource
    WebCaseMapper webCaseMapper;

    @Resource
    WebPlatformMapper webPlatformMapper;


    @Resource
    WebCasePlatformMapper webCasePlatform;


    @Resource
    WebUiAssertTypeMapper webUiAssertTypeMapper;

    @Resource
    WebUiAssertTypeNameMapper webUiAssertTypeNameMapper;

    @Override
    public List<WebCase> getListWebCase(WebCase webCase) {

        return webCaseMapper.getWebCaseList(webCase);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addWebCase(WebCase webCase) {
        try {
            int isInsert = webCaseMapper.addWebCase(webCase);
            if (isInsert > 0) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("数据库操作出现异常");
        }
        return false;
    }


    public List<WebPlatform> getPlatformById(Integer id) {
        return webCasePlatform.getPlatformById(id);
    }


    public List<AssertType> getAssertType(AssertType assertType) {
        return webUiAssertTypeMapper.getAssertTypeList(assertType);
    }

    public List<AssertTypeName> getAssertTypeName(AssertTypeName assertTypeName) {
        return webUiAssertTypeNameMapper.getAssertTypeListName(assertTypeName);
    }


    public JSONObject getWebCaseForUpdate(WebCase webCase) throws DataException {
        List<WebCase> webCases = webCaseMapper.getWebCaseList(webCase);
        if (webCases.size() > 1 || webCases.size() == 0) {
            throw new DataException("查询案例条数异常");
        }
//        int assertTypeId = 0;
        WebCase webCaseResult = webCases.get(0);
//        JSONArray caseStepArray = JSONArray.parseArray(webCaseResult.getCaseStep());
//        for (Object caseStepObj : caseStepArray) {
//            JSONObject caseStepJson = (JSONObject) caseStepObj;
//            if (caseStepJson.containsKey("assertType")) {
//                Integer id = caseStepJson.getInteger("assertType");
//                AssertTypeName assertTypeName = new AssertTypeName();
//                assertTypeName.setId(id);
//                List<AssertTypeName> assertTypeNames = webUiAssertTypeNameMapper.getAssertTypeListName(assertTypeName);
//                if (assertTypeNames.size() > 1 || assertTypeNames.size() == 0) {
//                    throw new DataException("查询断言类型名字异常");
//                }
//                AssertTypeName assertTypeNameResult = assertTypeNames.get(0);
//                caseStepJson.put("assertTypeName", assertTypeNameResult.getName());
//            }
//        }
//        webCaseResult.setCaseStep(caseStepArray.toJSONString());

        //获取所有assertTypeName
        AssertTypeName assertTypeName = new AssertTypeName();
        List<AssertTypeName> assertTypeNames = webUiAssertTypeNameMapper.getAssertTypeListName(assertTypeName);
        //获取所有AssertType
        AssertType assertType = new AssertType();
        List<AssertType> assertTypes = webUiAssertTypeMapper.getAssertTypeList(assertType);
        //获取所有Operation
        Map<String, String> operations = TestOperation.enumToList();
        //获取所有TestStep
        Map<String, String> testSteps = TestStep.enumToList();

        JSONObject res = JSONObject.parseObject(JSONObject.toJSONString(webCaseResult));
        res.put("assertTypeNames", assertTypeNames);
        res.put("assertTypes", assertTypes);
        res.put("operations", operations);
        res.put("testSteps", testSteps);
        return res;
    }


    @Override
    @Transactional(rollbackFor = DataException.class)
    public boolean deleteCase(Integer id) {
        int isDel = webCaseMapper.deleteCase(id);
        if (isDel < 1) {
            throw new DataException("更新数据失败");
        }
        return true;
    }


    /**
     * 案例整个过程
     *
     * @param webCase
     */
    @Transactional(propagation = Propagation.NESTED)
    public void seleniumExecuteByWebCase(WebCase webCase, WebReportDesc webReportDesc) {
        WebDriver webDriver = null;
        try {
            webReportDesc.setCaseName(webCase.getCaseDesc());
            webReportDesc.setUrl(webCase.getUrl());
            webDriver = initWebDriver(webCase, webCase.getWebPlatform().getPlatformName() + "_" + webCase.getPlatformId());
            //打开网页
            seleniumApiUtil.getUrl(webDriver, webCase.getUrl());
            String caseStep = webCase.getCaseStep();
            if (!judgeJSONArray(caseStep)) {
                throw new TypeCastException("请检查：caseStep");
            }
            JSONArray caseStepJsonArray = JSONObject.parseArray(caseStep);
            executeSingleWebCase(caseStepJsonArray, webCase, webDriver, webReportDesc);
            int upScreenshot = webCaseMapper.updateTestStatusOrScreenshot(webCase.getId(), '2', "/image/unexecutedCase.jpg");
            if (upScreenshot < 1) {
                throw new DataException();
            }
            webReportDesc.setStatus('2');
        } catch (InitBrowserException e) {
            webReportDesc.setStatus('3');
            webReportDesc.setErrorMsg("初始化浏览器失败");
            webCaseMapper.updateTestStatusOrScreenshot(webCase.getId(), '3', "/image/unexecutedCase.jpg");
        } catch (FindElementException e) {
            webReportDesc.setStatus('3');
            webReportDesc.setShotPic("/image/" + webCase.getCaseDesc() + webCase.getId() + ".jpg");
            webCaseMapper.updateTestStatusOrScreenshot(webCase.getId(), '3', "/image/" + webCase.getCaseDesc() + webCase.getId() + ".jpg");
        } catch (OpenUrlException e) {
            webReportDesc.setStatus('3');
            webReportDesc.setErrorMsg("网页打开失败，请检查url");
            webCaseMapper.updateTestStatusOrScreenshot(webCase.getId(), '3', "/image/unexecutedCase.jpg");
        } catch (TypeCastException e) {
            log.info(e);
            webReportDesc.setStatus('3');
            webReportDesc.setErrorMsg("案例解析失败，请检查案例内容");
            webCaseMapper.updateTestStatusOrScreenshot(webCase.getId(), '3', "/image/unexecutedCase.jpg");
        } catch (Exception e) {
            log.info(e);
            webReportDesc.setStatus('3');
            webReportDesc.setErrorMsg("未知异常");
            webCaseMapper.updateTestStatusOrScreenshot(webCase.getId(), '3', "/image/unexecutedCase.jpg");
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }

    }

    @Override
    public boolean addPlatform(WebPlatform webPlatform) {
        int addRes = 0;
        if (webPlatform.getId() == null) {
            addRes = webPlatformMapper.addWebPlatform(webPlatform);
        } else {
            addRes = webPlatformMapper.updateWebPlatform(webPlatform);
        }
        if (addRes <= 0) {
            return false;
        } else {
            return true;
        }

    }

    @Override
    public boolean updatePlatform(WebPlatform webPlatform) {
        int addRes = webPlatformMapper.updateWebPlatform(webPlatform);
        if (addRes <= 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<WebPlatform> getPlatformList(WebPlatform webPlatform) {
        List<WebPlatform> webPlatforms = webPlatformMapper.getPlatformList(webPlatform);
        return webPlatforms;
    }

    /**
     * 案例执行过程
     *
     * @param caseStepJsonArray
     * @param webCase
     * @param webDriver
     */
    @Transactional(rollbackFor = DataException.class)
    public void executeSingleWebCase(JSONArray caseStepJsonArray, WebCase webCase, WebDriver webDriver, WebReportDesc webReportDesc) {
        List<String> oldTags = new ArrayList<>();
        //保存当前打开页面tag信息
        oldTags.add(webDriver.getWindowHandle());
        for (int i = 0; i < caseStepJsonArray.size(); i++) {
            JSONObject caseStepJson = caseStepJsonArray.getJSONObject(i);
            Integer selectStep = caseStepJson.getInteger("selectStep");
            Integer operationStep = caseStepJson.getInteger("operationStep");
            Integer assertStep = caseStepJson.getInteger("assertStep");
            Integer assertType = caseStepJson.getInteger("assertType");
            String selectStepValue = null;
            if (selectStep > 0) {
                selectStepValue = webUiAssertTypeNameMapper.getAssertById(selectStep).getName();
            }
            String stepContentValue = caseStepJson.getString("stepContent");
            //String operationStepValue = TestOperation.getByValue(operationStep);
            String operationContentValue = caseStepJson.getString("operationContent");
            //String assertStepValue = webUiAssertTypeMapper.getAssertTypeById(assertStep).getName();
            String assertTypeValue = null;
            if (assertType > 0) {
                assertTypeValue = webUiAssertTypeNameMapper.getAssertById(assertType).getName();
            }
            String assertContentValue = caseStepJson.getString("assertContent");
            WebElement webElement = null;
            //选取html
            if ((selectStepValue != null && !selectStepValue.equals("")) && (stepContentValue != null && !stepContentValue.equals(""))) {
                webElement = seleniumApiUtil.findElement(stepContentValue, selectStepValue, webCase.getCaseDesc() + webCase.getId(), webDriver);
                if (webElement == null) {
                    webReportDesc.setErrorMsg("步骤寻找元素失败：" + (i + 1) + "步骤寻找元素：" + stepContentValue);
                    throw new FindElementException("查找元素失败，元素：" + stepContentValue);
                }
            }

            //html操作
            if (webElement != null && operationStep == 2) {
                webElement.click();
            } else if (webElement != null && operationStep == 3) {
                webElement.sendKeys(operationContentValue);
            } else if (webElement != null && operationStep == 4) {
                Actions actions = new Actions(webDriver);
                WebElement endWebElement = seleniumApiUtil.findElement(operationContentValue, selectStepValue, webCase.getCaseDesc() + webCase.getId(), webDriver);
                if (endWebElement == null) {
                    webReportDesc.setErrorMsg("操作失败：" + (i + 1) + "操作寻找元素：" + operationContentValue);
                    throw new FindElementException("查找元素失败，元素：" + operationContentValue);
                }
                actions.dragAndDrop(webElement, endWebElement);
            }
            //新窗口
            Set<String> tags = webDriver.getWindowHandles();
            a:
            for (String tag : tags) {
                if (!oldTags.contains(tag)) {
                    oldTags.add(tag);
                    webDriver = webDriver.switchTo().window(tag);
                    break a;
                }
            }


            //断言封装
            if (assertStep != null && assertStep == 1) {
                WebElement assertElement = seleniumApiUtil.findElement(assertContentValue, assertTypeValue, webCase.getCaseDesc() + webCase.getId(), webDriver);
                if (assertElement == null) {
                    webReportDesc.setErrorMsg("断言失败：" + (i + 1) + "断言寻找元素：" + assertContentValue);
                    throw new FindElementException("查找元素失败，元素：" + assertContentValue);
                }
            }
        }
    }


    /**
     * 判断字符串是否为jsonArray
     *
     * @param json
     * @return boolean
     */
    public boolean judgeJSONArray(String json) {
        try {
            JSONObject.parseArray(json);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 初始化浏览器webCase
     *
     * @param webCase
     * @return
     */
    public WebDriver initWebDriver(WebCase webCase, String cookieNames) {
        try {
            WebDriver webDriver = seleniumApiUtil.initLocalSelenium();
            //判断是否需要登录
            char needLogin = webCase.getNeedLogin();
            if (needLogin == '1') {
                seleniumApiUtil.setCookie(webDriver, webCase.getWebPlatform().getLoginUrl(), webCase.getWebPlatform().getLoginParams(), webCase.getWebPlatform().getBodyType() == '0' ? "form" : "raw", cookieNames);
            }
            return webDriver;
        } catch (Exception e) {
            log.error(e);
            throw new InitBrowserException();
        }

    }
}
