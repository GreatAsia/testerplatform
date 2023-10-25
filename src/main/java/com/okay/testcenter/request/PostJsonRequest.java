package com.okay.testcenter.request;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.middle.ResponseSampler;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.okay.testcenter.tools.ExceptionUtil.getMessage;
import static com.okay.testcenter.tools.ReplaceHtml.replaceHtml;
import static com.okay.testcenter.tools.UicodeBackslashU.unicodeToCn;
import static io.restassured.RestAssured.given;

public class PostJsonRequest extends RequestBase {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    ResponseSampler responseSampler = new ResponseSampler();
    private final static int CODE = 200;

    @Override
    public ResponseSampler post(RequestSampler requestSampler) {
        Response response = null;
        JudeHeader(requestSampler);
        logger.info("[caseName]==" + requestSampler.getCaseName());
        logger.info("[requestUrl]==" + requestSampler.getUrl());
        logger.info("[requestId]==" + requestSampler.getRequestId());
        logger.info("[requestHeaders]==" + requestSampler.getHeaders());
        logger.info("[requestCookies]==" + requestSampler.getCookies());

        String params = JSONObject.toJSONString(requestSampler.getBody());
        logger.info("[requestInfo]==" + params);
        try {
            response = given()
                    .cookies(requestSampler.getCookies())
                    .headers(requestSampler.getHeaders())
                    .contentType(requestSampler.getContentType())
                    .body(params)
                    .when()
                    .post(requestSampler.getUrl());


            if (response.getStatusCode() == CODE) {
                responseSampler.setCookies(response.getCookies());
                logger.info("[responseHeaders]==" + JSONObject.toJSONString(response.getHeaders()));
                logger.info("[responseCookies]==" + JSONObject.toJSONString(response.getCookies()));
                logger.info("[responseInfo]==" + response.asString());

            }else {

                logger.error("[requestInfo]==" + requestSampler.getBody());
                logger.error("[responseInfo]==" + response.asString() );
            }
                responseSampler.setRequestId(requestSampler.getRequestId());
                responseSampler.setResponseCode(response.getStatusCode());
            String result = replaceHtml(response.asString());
            if (result.contains("\\u")) {
                result = unicodeToCn(result);
            }
            responseSampler.setResponse(result);

            responseSampler.setUrl(requestSampler.getUrl());
            responseSampler.setCheckData(requestSampler.getCheckData());
            responseSampler.setInterfaceId(requestSampler.getInterfaceId());
            responseSampler.setCaseName(requestSampler.getCaseName());

        } catch (Exception e) {
            logger.error("[报错用例]=={}", requestSampler.getCaseName());
            logger.error("[报错数据]=={}", JSONObject.toJSONString(requestSampler));
            logger.error("[requestFail]=={}", getMessage(e));

            return new ResponseSampler();
        }

        return responseSampler;

    }
}
