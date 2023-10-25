package com.okay.testcenter.tools.webUi;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.HttpUtilResultBean;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtil {
    /**
     * get请求
     *
     * @param url
     * @param headers
     * @return
     */
    public static HttpUtilResultBean doGet(String url, JSONObject headers) {
        HttpUtilResultBean httpUtilResultBean = new HttpUtilResultBean();
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";


        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                // 设置header
                for (String key : headers.keySet()) {
                    httpGet.setHeader(key, headers.getString(key));
                }
            }
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    //.setRedirectsEnabled(false)
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeHttp(response, httpClient);
            httpUtilResultBean.setCookies(cookieStore.getCookies());
            httpUtilResultBean.setResult(response.getEntity().toString());
            httpUtilResultBean.setStatus(response.getStatusLine().getStatusCode());
        }
        return httpUtilResultBean;
    }


    /**
     * post请求
     *
     * @param url      url
     * @param paramMap paramMap
     * @param headers  headers
     * @return 结果
     */
    public static HttpUtilResultBean doPost(String url, JSONObject paramMap, JSONObject headers, String requestType) {
        HttpUtilResultBean httpUtilResultBean = new HttpUtilResultBean();
        List<Cookie> cookies = new ArrayList<>();
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        CookieStore cookieStore = new BasicCookieStore();
        // 创建httpClient实例
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        if (headers != null) {
            // 设置header
            for (String key : headers.keySet()) {
                httpPost.setHeader(key, headers.getString(key));
            }
        }

        try {
            // 为httpPost设置封装好的请求参数
            if (requestType != null && requestType.equals("raw")) {
                StringEntity s = new StringEntity(paramMap.toString(), "utf-8");
                s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                        "application/json"));
                httpPost.setEntity(s);
            } else {
                httpPost.setEntity(new UrlEncodedFormEntity(getParam(paramMap), "UTF-8"));
            }

            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                httpUtilResultBean.setResult(EntityUtils.toString(httpResponse.getEntity(),"UTF-8"));
                httpUtilResultBean.setStatus(httpResponse.getStatusLine().getStatusCode());
            }
            if (httpResponse.getStatusLine().getStatusCode() == 302) {
                HttpUtilResultBean httpUtilResultGet = HttpUtil.doGet(httpResponse.getFirstHeader("Location").getValue(), new JSONObject());
                cookies.addAll(httpUtilResultGet.getCookies());
                httpUtilResultBean.setResult(EntityUtils.toString(httpResponse.getEntity(),"UTF-8"));
                httpUtilResultBean.setStatus(httpUtilResultGet.getStatus());
            }
            cookies.addAll(cookieStore.getCookies());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeHttp(httpResponse, httpClient);
            httpUtilResultBean.setCookies(cookies);
        }
        return httpUtilResultBean;
    }


    /**
     * 参数处理
     *
     * @param paramMap
     * @return
     */
    public static List<NameValuePair> getParam(JSONObject paramMap) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
            // 通过map集成entrySet方法获取entity
            Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> mapEntry = iterator.next();
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }
        }
        return nvps;
    }

    public static void closeHttp(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
        // 关闭资源
        if (null != httpResponse) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }
}
