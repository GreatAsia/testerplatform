package com.okay.testcenter.domain.middle;


import com.okay.testcenter.domain.bean.Base;
import io.restassured.http.Headers;

import java.util.HashMap;
import java.util.Map;


public class ResponseSampler extends Base {


    private String requestId;
    private Map<String, String> cookies = new HashMap<>();
    private String url;
    private String token;
    private Headers headers;
    private String response;
    private String startTime;
    private String endTime;
    private int responseCode;
    private String uName;
    private String urlHeader;
    private String result;

    private String elapsedTime;
    private Boolean loginResult = false;

    private String requestData;

    private String checkData;
    private int interfaceId;
    private String caseName;


    public String getCheckData() {
        return checkData;
    }

    public void setCheckData(String checkData) {
        this.checkData = checkData;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }


    public String getUrlHeader() {
        return urlHeader;
    }

    public void setUrlHeader(String urlHeader) {
        this.urlHeader = urlHeader;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public Boolean getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(Boolean loginResult) {
        this.loginResult = loginResult;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }


    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

}
