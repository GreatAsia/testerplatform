package com.okay.testcenter.tools.applogin;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.OuathBean;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.middle.ResponseSampler;
import com.okay.testcenter.request.RequestFactory;
import com.okay.testcenter.tools.UserRSAUtil;
import io.restassured.http.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static jodd.util.ThreadUtil.sleep;

/**
 * @author zhou
 * @date 2021/8/9
 */
public class Login {
    private static final Logger logger = LoggerFactory.getLogger(Login.class);
    private static String urlHeader = "http://qa-test.igetcool.com";
    //    private String urlHeader = "https://gateway-server-beta.svc.igetcool.com";
    private static String urlPath = "/app-api-user-server/white/code/login/v2.json";
    private static String sendUrl = "/app-api-user-server/white/sms/smscode/v1.json";
    private final static String CHARSET_NAME = "utf-8";
    private final static String TRANSFORMATION = "AES/ECB/PKCS5Padding";



    public OuathBean getOuath(String host,String loginPhone) throws UnsupportedEncodingException {

        String phone = UserRSAUtil.encrypt(loginPhone);
        String countrycode = UserRSAUtil.encrypt("86");

        String phoneEncode = URLEncoder.encode(phone,"UTF-8");
        String countrycodeEncode = URLEncoder.encode(countrycode,"UTF-8");

        Map sendHeader = new HashMap();
        sendHeader.put("App-Version","4.6.0");

        //发送验证码
        String sendparam = "phone="+ phone + "&smstype=0&countrycode=" + countrycode + "&type=1";
        RequestSampler sRequestSampler = new RequestSampler();
        sRequestSampler.setUrl_header(host);
        sRequestSampler.setUrl(host + sendUrl);
        sRequestSampler.setMethod("Get");
        sRequestSampler.setHeaders(sendHeader);
        sRequestSampler.setParams(sendparam);
        ResponseSampler sResponseSampler = RequestFactory.build(sRequestSampler);
        logger.info("sResponseSampler=={}",sResponseSampler.getResponse());
        sleep(1000);
        //登录
        Map header = new HashMap();
        header.put("App-Device-Key","827e63832cfc14ab");
        header.put("contentType","application/x-www-form-urlencoded");
        String param = "smscode=8888&phone=" + phoneEncode + "&countrycode=" + countrycodeEncode;
        RequestSampler requestSampler = new RequestSampler();
        requestSampler.setUrl_header(host);
        requestSampler.setUrl(host + urlPath);
        requestSampler.setMethod("Post-Form");
        requestSampler.setHeaders(header);
        requestSampler.setParams(param);
        ResponseSampler responseSampler = RequestFactory.build(requestSampler);
        if (!responseSampler.getResponse().contains("10000")){
            logger.info("ouathBean==null");
            return new OuathBean();
        }
        String data = JSONObject.parseObject(responseSampler.getResponse()).getString("data");

        logger.info("responseSampler=={}",responseSampler.getResponse());
        logger.info("responseHeaders=={}",JSONObject.toJSONString(responseSampler.getHeaders()));
        logger.info("data=={}",data);
        Headers headers = responseSampler.getHeaders();

        String atime = headers.getValue("App-Atime");
        String nonce = headers.getValue("App-Nonce");
        String sKey = parseSign(atime,nonce);
        String ouath = Decrypt(data,sKey);
        logger.info("ouath=={}",ouath);
        JSONObject ouathObject = JSONObject.parseObject(ouath);
        JSONObject userObject = JSONObject.parseObject(ouathObject.getString("user"));

        String phone_number =  userObject.getString("identity");
        String app_user_pid =  userObject.getString("pid");
        String app_oauth = ouathObject.getString("jwt");
        logger.info("phone_number=={},app_user_pid={},app_oauth={}",phone_number,app_user_pid,app_oauth);
        OuathBean ouathBean  = new OuathBean();
        ouathBean.setPhone(phone_number);
        ouathBean.setAppUserPid(app_user_pid);
        ouathBean.setAppOauth(app_oauth);
        logger.info("ouathBean=={}",JSONObject.toJSONString(ouathBean));
        return ouathBean;
    }


    /**
     * 解析头部
     */
    public String parseSign(String atime,String nonce) {
        String tempATime = atime.substring(5, 13);
        String tempNonce = nonce.substring(5, 13);
        return tempATime + tempNonce;
    }


    /**
     * 使用参数中的密钥解密
     *
     * @param sSrc 密文
     * @param sKey 密钥
     * @return 明文
     */
    public static String Decrypt(String sSrc, String sKey) {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                logger.info("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                logger.info("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes(CHARSET_NAME);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            // BASE64解密
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encrypted1 = decoder.decode(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, CHARSET_NAME);
                return originalString;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
