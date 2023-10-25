package com.okay.testcenter.common.enumFile.webUi;

import org.springframework.util.ClassUtils;

public enum AboutSeleniumDriver {
    CHROME_DRIVER("chrome","webdriver.chrome.driver",AboutSeleniumDriver.class.getResource("/").getFile()+"browserDriver/chromedriver.exe"),
    FIREFOX_DRIVER("firefox","webdriver.firefox.marionette","D:\\dirver\\"),
    IE_DRIVER("IE","","D:\\dirver\\"),
    WIN_PLANTOMJS_DRIVER("phantomjs","phantomjs.binary.path", ClassUtils.getDefaultClassLoader().getResource("").getPath()+"browserDriver/phantomjs-2.1.1-windows\\bin\\phantomjs.exe"),
    LINUX_PLANTOMJS_DRIVER("phantomjs","phantomjs.binary.path","/xdfapp/packet/phantomjs/bin/phantomjs");

    private String aliaDriverName;
    private String driverName;
    private String driverPath;

    AboutSeleniumDriver(String aliaDriverName,String driverName,String driverPath){
        this.aliaDriverName=aliaDriverName;
        this.driverName=driverName;
        this.driverPath=driverPath;
    }

    public String getAliaDriverName(){
        return this.aliaDriverName;
    }

    public String getDriverName(){
        return this.driverName;
    }

    public String getDriverPath(){
        return this.driverPath;
    }
}
