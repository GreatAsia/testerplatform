package com.okay.testcenter;

import com.github.pagehelper.PageHelper;
import com.okay.testcenter.log.LogMDC.MyLogFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;
import java.util.Properties;


@EnableScheduling
@MapperScan({"com.okay.testcenter.mapper", "com.okay.testcenter.config"})
@SpringBootApplication
@EnableAsync
public class TestcenterApplication {


    public static void main(String[] args) {
        SpringApplication.run(TestcenterApplication.class, args);

    }


    /**
     * 配置mybatis的分页插件pageHelper
     *
     * @return
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        //配置mysql数据库的方言
        properties.setProperty("dialect", "mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

    /**
     * 解决文件上传,临时文件夹被程序自动删除问题
     * <p>
     * 文件上传时自定义临时路径
     *
     * @return
     */
    @Value("${web.upload-path}")
    private String filePath;
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //2.该处就是指定的路径(需要提前创建好目录，否则上传时会抛出异常)
        factory.setLocation(filePath);
        return factory.createMultipartConfig();
    }


    @Bean
    public FilterRegistrationBean logFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean<>();
        //注入过滤器
        registration.setFilter(new MyLogFilter());
        //拦截规则
        registration.addUrlPatterns("/*");
        //过滤器名称
        registration.setName("MyLogFilter");
        //过滤器顺序
        registration.setOrder(0);
        return registration;
    }

}
