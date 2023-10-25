package com.okay.testcenter.tools;



/**
 * @author zhou
 * @date 2020/8/21
 */
public class ReplaceHtml {


    public static String replaceHtml(String str){
        if (str == null || str.length() == 0){
            return "";
        }
        return str.replace("<","&lt").replace(">","&gt;").replace("&","&amp;");
    }

}
