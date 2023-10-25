package com.okay.testcenter.controller.login;


import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.user.UserRequest;
import com.okay.testcenter.service.user.UserService;
import com.okay.testcenter.tools.singlelogin.SingleLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(description = "登录接口")
@Controller

public class LoginController {


    @Autowired
    UserService userService;

    @GetMapping(value = "/")
    public void defIndex(HttpServletRequest request,HttpServletResponse response) throws Exception{
        WebUtils.issueRedirect(request, response, "/login");
    }

    @GetMapping(value = "/login")
    public String index() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated() || currentUser.isRemembered()) {
            Object jump= currentUser.getSession().getAttribute("shiroSavedRequest");
            String jumpUrl = jump==null?"/api/home": ((SavedRequest)jump).getRequestUrl();
            return "redirect:"+jumpUrl;
        } else {
            return "html/login";
        }
    }

    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping(value = "/login")
    @ResponseBody
    public RetResult login(UserRequest user) {

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword() ,true);
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
//            log.info("There is no user with username of " + token.getPrincipal());
            return RetResponse.makeErrRsp("用户认证失败");
        } catch (IncorrectCredentialsException ice) {
//            log.info("Password for account " + token.getPrincipal() + " was incorrect!");
            return RetResponse.makeErrRsp("用户名密码不匹配");
        } catch (LockedAccountException lae) {
//            log.info("The account for username " + token.getPrincipal() + " is locked.  " +
//                    "Please contact your administrator to unlock it.");
            return RetResponse.makeErrRsp("当前用户已锁定，请等待5分钟后重试");
        }
        // ... catch more exceptions here (maybe custom ones specific to your application?
        catch (AuthenticationException ae) {
            //unexpected condition?  error?
            return RetResponse.makeErrRsp("授权失败");
        }
        Object jump= currentUser.getSession().getAttribute("shiroSavedRequest");
        String jumpUrl = jump==null?"/api/home": ((SavedRequest)jump).getRequestUrl();
        Map<String, String> jumpMap = new HashMap<>();
        jumpMap.put("jumpUrl",jumpUrl);
        return RetResponse.makeRsp(200,"登录成功",jumpMap);
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return "redirect:/login";
    }

    @ApiOperation(value = "单点登录", notes = "单点登录")
    @PostMapping(value = "/singleLogin")
    @ResponseBody
    public RetResult<Object> singleLogin(@RequestBody JSONObject request, HttpServletResponse response) {

        String url = request.getString("url");
        String userName = request.getString("userName");
        String passWord = request.getString("passWord");
        Map<String, String> cookies = new SingleLogin(url, userName, passWord).login().getCookies();
        if (cookies == null) {
            return RetResponse.makeErrRsp("登录失败");
        } else {

            for (Object str : cookies.keySet()) {

                String host = url.split("//")[1].trim();
                //给cookie设置超时时间 +1小时
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.HOUR, 1);

                Cookie cookie = new Cookie(str.toString(), cookies.get(str));
                cookie.setDomain(host);
                cookie.setPath("/");
                cookie.setSecure(true);
                cookie.setVersion(0);
                cookie.setMaxAge(60 * 60);

                response.addCookie(cookie);
            }
            StringBuilder stringBuilder = new StringBuilder();

            Set<String> set = cookies.keySet();
            for (String key : set) {
                stringBuilder.append(key).append("=")
                             .append(cookies.get(key).toString()).append(";");
              }
            String cookiesString =  stringBuilder.toString().substring(0, stringBuilder.lastIndexOf(";"));



            return RetResponse.makeOKRsp(cookiesString);
        }


    }


    @GetMapping(value = "/404")
    public String to404(){
        return "html/page-error-404";
    }

    @GetMapping(value = "/405")
    public String to405(){
       return "html/page-error-405";
    }


}
