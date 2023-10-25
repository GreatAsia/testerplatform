package com.okay.testcenter;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.middle.RequestSampler;
import com.okay.testcenter.domain.middle.ResponseSampler;
import com.okay.testcenter.request.RequestFactory;
import com.okay.testcenter.tools.UserRSAUtil;
import io.restassured.http.Headers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import java.util.Base64;
import static com.okay.testcenter.request.RequestFactory.build;
import static jodd.util.ThreadUtil.sleep;
import static sun.security.x509.CertificateAlgorithmId.ALGORITHM;

/**
 * @author zhou
 * @date 2021/7/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IgetcoolLoginTest {
    private static final Logger logger = LoggerFactory.getLogger(IgetcoolLoginTest.class);
    private String urlHeader = "http://qa-test.igetcool.com";
//    private String urlHeader = "https://gateway-server-beta.svc.igetcool.com";
    private String urlPath = "/app-api-user-server/white/code/login/v2.json";
    private String sendUrl = "/app-api-user-server/white/sms/smscode/v1.json";
    private final static String CHARSET_NAME = "utf-8";
    private final static String TRANSFORMATION = "AES/ECB/PKCS5Padding";


    @Test
    public void testLogin() throws UnsupportedEncodingException {

        String phone = UserRSAUtil.encrypt("12300000017");
        String countrycode = UserRSAUtil.encrypt("86");

        String phoneEncode = URLEncoder.encode(phone,"UTF-8");
        String countrycodeEncode = URLEncoder.encode(countrycode,"UTF-8");

        Map sendHeader = new HashMap();
        sendHeader.put("App-Version","4.6.0");

        //发送验证码
        String sendparam = "phone="+ phone + "&smstype=0&countrycode=" + countrycode + "&type=1";
        RequestSampler sRequestSampler = new RequestSampler();
        sRequestSampler.setUrl_header(urlHeader);
        sRequestSampler.setUrl(urlHeader + sendUrl);
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
        requestSampler.setUrl_header(urlHeader);
        requestSampler.setUrl(urlHeader + urlPath);
        requestSampler.setMethod("Post-Form");
        requestSampler.setHeaders(header);
        requestSampler.setParams(param);
        ResponseSampler responseSampler = RequestFactory.build(requestSampler);
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
            // byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
            // BASE64解密
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encrypted1 = decoder.decode(sSrc);
//            byte[] encrypted1 = Base64.decode(sSrc, Base64.DEFAULT);
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
