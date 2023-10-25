package com.okay.testcenter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class StaticConfiguration implements WebMvcConfigurer {

    @Value("${img.path}")
    private String imgPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 对文件的路径进行配置,创建一个虚拟路径/file/** ，即只要在<img src="/image/xxx.jpg" />便可以直接引用图片
         * 这是图片的物理路径 "file:/+本地图片的地址"
         *
         */
        /*registry.addResourceHandler("/image/**").addResourceLocations("file:/xdfapp/apppics/");*/
        /*registry.addResourceHandler("/image/**").addResourceLocations("file:" + imgPath, "file:" + System.getProperty("user.dir") + "/src/main/resources/static/image/");*/
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + imgPath, "classPath:static/image/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //主页
        registry.addViewController("/").setViewName("forward:/login");
    }


}
