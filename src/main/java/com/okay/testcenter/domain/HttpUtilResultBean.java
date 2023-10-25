package com.okay.testcenter.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * @author XieYangYang
 * @date 2020/6/8 9:52
 */
@Getter
@Setter
public class HttpUtilResultBean {

    Integer status;
    List<Cookie> cookies;
    String result;
}
