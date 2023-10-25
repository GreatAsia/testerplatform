package com.okay.testcenter.tools.webUi;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.common.enumFile.webUi.AboutSeleniumDriver;
import com.okay.testcenter.domain.ui.WebCase;
import com.okay.testcenter.exception.myException.OpenUrlException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

import static io.restassured.RestAssured.given;

@Service("seleniumApiUtil")
public class SeleniumApiUtil {
    Logger log = Logger.getLogger(SeleniumApiUtil.class);

    @Autowired
    MyPhantomJSDriverService myPhantomJSDriverService;

    public WebDriver initLocalSelenium() {
        WebDriver webDriver = null;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            DesiredCapabilities dcaps = dcaps(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, AboutSeleniumDriver.WIN_PLANTOMJS_DRIVER.getDriverPath());
            webDriver = new PhantomJSDriver(myPhantomJSDriverService.createDefaultService(dcaps), dcaps);

        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            DesiredCapabilities dcaps = dcaps(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, AboutSeleniumDriver.LINUX_PLANTOMJS_DRIVER.getDriverPath());
            webDriver = new PhantomJSDriver(myPhantomJSDriverService.createDefaultService(dcaps), dcaps);
        }
        webDriver.manage().window().maximize();
        return webDriver;
    }


    /**
     * 打开网页
     *
     * @param webDriver
     * @param url
     */
    public void getUrl(WebDriver webDriver, String url) {
        if (!isURL(url)) {
            throw new OpenUrlException();
        }
        webDriver.get(url);
    }

    /**
     * 判断是否是一个url
     *
     * @param str
     * @return
     */
    public static boolean isURL(String str) {
        try {
            URL url = new URL(str);
            InputStream in = url.openStream();
        } catch (Exception e1) {
            return false;
        }
        return true;
    }

    /**
     * 初始化selenium
     *
     * @param browser   浏览器名字
     * @param dimension 浏览器窗口
     * @return WebDriver
     */
    public WebDriver initLocalSelenium(String browser, Dimension dimension) {
        WebDriver webDriver = null;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            switch (browser) {
                case "firefox":
                    System.setProperty(AboutSeleniumDriver.FIREFOX_DRIVER.getDriverName(), AboutSeleniumDriver.FIREFOX_DRIVER.getDriverPath());
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    webDriver = new FirefoxDriver(firefoxOptions);
                    break;
                case "chrome":
                    System.setProperty(AboutSeleniumDriver.CHROME_DRIVER.getDriverName(), AboutSeleniumDriver.CHROME_DRIVER.getDriverPath());
                    ChromeOptions chromeOptions = new ChromeOptions();
                    webDriver = new ChromeDriver(chromeOptions);
                    break;
                case "IE":
                    System.setProperty(AboutSeleniumDriver.IE_DRIVER.getDriverName(), AboutSeleniumDriver.IE_DRIVER.getDriverPath());
                    InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
                    webDriver = new InternetExplorerDriver(internetExplorerOptions);
                    break;
                case "phantomjs":
                    webDriver = new PhantomJSDriver(dcaps(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, AboutSeleniumDriver.WIN_PLANTOMJS_DRIVER.getDriverPath()));
                    break;
            }
        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            switch (browser) {
                case "phantomjs":
                    webDriver = new PhantomJSDriver(dcaps(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, AboutSeleniumDriver.LINUX_PLANTOMJS_DRIVER.getDriverPath()));
                    break;
            }
        }
        if (dimension == null) {
            webDriver.manage().window().maximize();
        } else {
            webDriver.manage().window().setSize(dimension);
        }
        return webDriver;
    }

    /**
     * phantomjs驱动管理
     *
     * @return DesiredCapabilities
     */
    public DesiredCapabilities dcaps(String phantomjsPath, String phantomjsAlias) {
        // 设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        // ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        // 截屏支持
        dcaps.setCapability("takesScreenshot", true);
        // css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        // js支持
        dcaps.setJavascriptEnabled(true);
        // 驱动支持
        dcaps.setCapability(phantomjsPath, phantomjsAlias);
        /* ------设置浏览器请求头 ------*/
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        return dcaps;
    }


    /**
     * 公用代码模块
     *
     * @param element 元素值
     * @param type    元素类型
     * @return 返回By
     */
    public static By commenBy(String element, String type) {
        By elementBy = null;
        switch (type) {
            case "id":
                elementBy = By.id(element);
                break;
            case "name":
                elementBy = By.name(element);
                break;
            case "class":
                elementBy = By.className(element);
                break;
            case "selector":
                elementBy = By.cssSelector(element);
                break;
            case "xpath":
                elementBy = By.xpath(element);
                break;
            case "linkText":
                elementBy = By.linkText(element);
                break;
        }
        return elementBy;
    }

    /**
     * 寻找单个元素
     *
     * @param element      元素值
     * @param type         元素类型
     * @param webDriver    WebDriver
     * @param elementAlias 元素别名
     * @return 元素对应值
     */
    public WebElement findElement(String element, String type, String elementAlias, final WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, 15);
        WebElement webElement = null;
        try {
            final By elementAboutBy = commenBy(element, type);
            webElement = wait.until(new Function<WebDriver, WebElement>() {
                @Override
                public WebElement apply(WebDriver d) {
                    return webDriver.findElement(elementAboutBy);
                }
            });
            log.info(elementAlias + "查找成功^_^：" + element);
        } catch (Exception e) {
            log.error(elementAlias + "查找失败-_-，元素值：" + element);
            screenShot(webDriver, elementAlias);
        }
        return webElement;
    }

    /**
     * 寻找多个元素
     *
     * @param element   元素值
     * @param type      元素类型
     * @param webDriver WebDriver
     * @return list元素集合
     */
    public List<WebElement> findElements(String element, String type, String elementAlias, final WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        List<WebElement> webElements = null;
        try {
            final By elementAboutBy = commenBy(element, type);
            webElements = wait.until(new Function<WebDriver, List<WebElement>>() {
                @Override
                public List<WebElement> apply(WebDriver d) {
                    return webDriver.findElements(elementAboutBy);
                }
            });
            log.info(elementAlias + "查找成功^_^");
        } catch (Exception e) {
            log.error(elementAlias + "查找失败-_-");
            screenShot(webDriver, elementAlias);
            webDriver.quit();
        }
        return webElements;
    }

    public File savePic(WebElement element, WebDriver driver, File picFile) {
        if (element == null) throw new NullPointerException("图片元素失败");
        WrapsDriver wrapsDriver = (WrapsDriver) element;    //截取整个页面
        File scrFile = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        try {
            BufferedImage img = ImageIO.read(scrFile);
            int screenshotWidth = img.getWidth();
            Dimension dimension = driver.manage().window().getSize();
            //获取浏览器尺寸与截图的尺寸
            double scale = (double) dimension.getWidth() / screenshotWidth;
            System.out.println(scale);
            int eleWidth = element.getSize().getWidth();
            int eleHeight = element.getSize().getHeight();
            Point point = element.getLocation();
            int subImgX = (int) (point.getX()); //获得元素的坐标
            int subImgY = (int) (point.getY());
            int subImgWight = (int) (eleWidth); //获取元素的宽高
            int subImgHeight = (int) (eleHeight);
            //精准的截取元素图片，
            BufferedImage dest = img.getSubimage(subImgX, subImgY, subImgWight, subImgHeight);
            ImageIO.write(dest, "png", picFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return picFile;
    }

    @Value("${img.path}")
    private String imgPath;

    @PostConstruct
    public void init() {
    }

    /**
     * 截屏
     *
     * @param driver         selenium驱动
     * @param screenShotName 截图名称
     */
    public void screenShot(WebDriver driver, String screenShotName) {
        File screenShotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String path = imgPath;
        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        try {
            FileUtils.copyFile(screenShotFile, new File(path + screenShotName + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //public Cookie

    /**
     * @param webDriver
     * @param url         url
     * @param param       请求参数
     * @param requestType raw是json请求
     */
    public void setCookie(WebDriver webDriver, String url, String param, String requestType, String cookieNames) {
        SingletonCookie singletonCookie = new SingletonCookie();
        Map<String, List<org.apache.http.cookie.Cookie>> mapCookies = singletonCookie.setCookies(cookieNames, url, param, requestType);
        List<org.apache.http.cookie.Cookie> cookies = mapCookies.get(cookieNames);
        Long timeStamp = System.currentTimeMillis() + 5000000;
        if (webDriver instanceof ChromeDriver) {
            webDriver.get(url);
        }
        for (org.apache.http.cookie.Cookie cookie : cookies) {
            webDriver.manage().addCookie(new Cookie(cookie.getName(), cookie.getValue(), "." + cookie.getDomain(), cookie.getPath(), new Date(Long.parseLong(String.valueOf(timeStamp)))));
        }
    }


    public class SingletonCookie {

        Map<String, List<org.apache.http.cookie.Cookie>> mapCookies = new HashMap<>();

        public Map<String, List<org.apache.http.cookie.Cookie>> setCookies(String cookieNames, String url, String param, String requestType) {
            if (mapCookies.get(cookieNames) == null) {
                List<org.apache.http.cookie.Cookie> cookies = HttpUtil.doPost(url, JSONObject.parseObject(param), JSONObject.parseObject("{'requestId':'login_123456'}"), requestType).getCookies();
                mapCookies.put(cookieNames, cookies);
            }
            return mapCookies;
        }

        public void cleanCookies(String cookieNames) {
            if (mapCookies.get(cookieNames) != null) {
                mapCookies.remove(cookieNames);
            }
        }
    }


}