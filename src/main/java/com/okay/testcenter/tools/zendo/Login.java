package com.okay.testcenter.tools.zendo;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.middle.ResponseSampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.okay.testcenter.request.RequestFactory.build;
import static com.okay.testcenter.tools.UicodeBackslashU.unicodeToCn;


/**
 * @author zhou
 * @date 2020/11/4
 * 登录禅道
 */
public class Login {

    private final static Logger logger = LoggerFactory.getLogger(Login.class);
    private static final String SESSION = "?m=api&f=getSessionID&t=json";
    private static final String LOGIN = "?m=user&f=login&t=json";


    public  String getSession(RequestSampler requestSampler){

        requestSampler.setMethod("Get");
        requestSampler.setUrl(requestSampler.getUrl_header() + SESSION);
        ResponseSampler responseSampler = build(requestSampler);
        JSONObject jsonObject = JSONObject.parseObject(responseSampler.getResponse());
        String sessionID = JSONObject.parseObject(jsonObject.get("data").toString()).get("sessionID").toString();
        logger.info("sessionID {}",sessionID);
        return sessionID;
    }

    public ResponseSampler login(RequestSampler requestSampler){

        String sessionID = new Login().getSession(requestSampler);
        Map<String,String> map = new HashMap<>();
        map.put("sessionID",sessionID);
        requestSampler.setHeaders(map);
        requestSampler.setUrl(requestSampler.getUrl_header() + LOGIN);
        requestSampler.setMethod("Post-Form");
        requestSampler.setParams("account=" + requestSampler.getUname() + "&password=" + requestSampler.getPwd());
        ResponseSampler responseSampler = build(requestSampler);
        if (responseSampler.getResponse().contains("success")){
           responseSampler.setResult("FAIL");
        }else {
            responseSampler.setResult("PASS");
        }
        return responseSampler;

    }


    public static void main(String[] args)  {
        RequestSampler requestSampler = new RequestSampler();
        requestSampler.setUrl_header("http://bug.okjiaoyu.cn/index.php");
        requestSampler.setUname("zhangyazhou");
        requestSampler.setPwd("123456");
        requestSampler.setProductId("138");
        ResponseSampler responseSampler = new Login().login(requestSampler);
        logger.info("response== {}",responseSampler.getResponse());
        requestSampler.setCookies(responseSampler.getCookies());
        ResponseSampler product = new EditBug().getModuleList(requestSampler);
        logger.info("module== {}",product.getResponse());



    }

}
