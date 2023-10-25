package com.okay.testcenter.service.webUi;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.Page;
import com.okay.testcenter.domain.ui.*;
import com.okay.testcenter.exception.myException.DataException;
import org.openqa.selenium.WebDriver;

import java.util.List;

public interface WebUIService {
    public List<WebCase> getListWebCase(WebCase webCase);

    public Boolean addWebCase(WebCase webCase);

    public List<WebPlatform> getPlatformById(Integer id);

    public List<AssertType> getAssertType(AssertType assertType);

    public List<AssertTypeName> getAssertTypeName(AssertTypeName assertTypeName);

    public JSONObject getWebCaseForUpdate(WebCase webCase) throws DataException;

    public boolean deleteCase(Integer id);

    public void seleniumExecuteByWebCase(WebCase webCase, WebReportDesc webReportDesc);

    public boolean addPlatform(WebPlatform webPlatform);

    public boolean updatePlatform(WebPlatform webPlatform);

    public List<WebPlatform> getPlatformList(WebPlatform webPlatform);
}
