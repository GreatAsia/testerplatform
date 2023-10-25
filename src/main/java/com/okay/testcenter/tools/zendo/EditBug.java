package com.okay.testcenter.tools.zendo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.middle.ResponseSampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.okay.testcenter.request.RequestFactory.build;

/**
 * @author zhou
 * @date 2020/11/4
 * 创建和关闭Bug
 */
public class EditBug {

    private final static String CLOSE = "?m=bug&f=close&t=json&bugID=";
    private final static String PRODUCTLIST = "?m=product&f=ajaxGetDropMenu&objectID=132&module=bug&method=browse&extra=&t=json";
    private final static String MODULELIST = "?m=tree&f=ajaxGetModules&t=json&productID=138&viewType=bug&branchID=0&number=0";
    private final static Logger logger = LoggerFactory.getLogger(Login.class);


    public  ResponseSampler createBug(RequestSampler requestSampler){

        requestSampler.setMethod("Post-Form");
        ResponseSampler responseSampler = build(requestSampler);
        if(responseSampler.getResponse().contains("code:0")){
            responseSampler.setResult("PASS");
        }else {
            responseSampler.setResult("FAIL");
        }
        return responseSampler;
    }

    public ResponseSampler closeBug(int bugId,RequestSampler requestSampler){

        requestSampler.setUrl(requestSampler.getUrl_header() + CLOSE + bugId);
        requestSampler.setMethod("Get");
        ResponseSampler responseSampler = build(requestSampler);
        if(responseSampler.getResponse().contains("code:0")){
            responseSampler.setResult("PASS");
        }else {
            responseSampler.setResult("FAIL");
        }

        return responseSampler;

    }


    public ResponseSampler getProductList(RequestSampler requestSampler){

        TreeMap map = new TreeMap();
        requestSampler.setUrl(requestSampler.getUrl_header() + PRODUCTLIST);
        requestSampler.setMethod("Get");
        requestSampler.setParams("");
        ResponseSampler responseSampler = build(requestSampler);
        if(responseSampler.getResponse().contains("\"status\":\"success\"")){

            String result = responseSampler.getResponse().replace("\\","");
            logger.info("result== {}",result);
            String data = "[" + result.split("\\[")[1].split("]")[0] + "]";
            JSONArray products = JSONObject.parseArray(data);
            for (int i= 0;i< products.size();++i){

                JSONObject array = products.getJSONObject(i);
                String name = array.getString("name");
                String id = array.getString("id");
                if(!name.contains("废弃")){
                    map.put(id,name);
                }

            }

            responseSampler.setResponse(JSONObject.toJSONString(map));
            responseSampler.setResult("PASS");
        }else {
            responseSampler.setResult("FAIL");
        }

        return responseSampler;

    }

    public ResponseSampler getModuleList(RequestSampler requestSampler){
        Map map = new HashMap();
        requestSampler.setUrl(requestSampler.getUrl_header() + MODULELIST.replace("138",requestSampler.getProductId()));
        requestSampler.setMethod("Get");
        requestSampler.setParams("");
        ResponseSampler responseSampler = build(requestSampler);
        String result = responseSampler.getResponse();
        String[] modules = result.split("\\n");
        for (String module: modules) {
            if(module.contains("option")){
                String moduleId = module.split("value='")[1].split("' title")[0].trim();
                String name = module.split("title='")[1].split("' data-keys")[0].replace("/","").trim();
                map.put(moduleId,name);
            }
        }
        responseSampler.setResponse(JSONObject.toJSONString(map));
        responseSampler.setResult("PASS");

        return responseSampler;
    }


}
