#支持API和 WEB自动化，配置有定时任务，实时查看日志，异常日志发送钉钉和邮件功能



#自动化测试平台 <br>

   * --com.okay.testcenter <br>
       * --common 公共的内容 <br>
           * --aop <br>
           * --constant <br>
           * --convert <br>
           * --enumFile.webUi <br>
       * --config 配置 <br>   
           * --StaticConfiguration 静态文件配置 <br>
           * --SwaggerConfig 静态文件配置 <br>
       * --controller 控制器 <br>           
       * --domain bean对象 <br>       
       * --exception统一异常处理 <br>     
       * --impl 实现类 <br>         
       * --job 定时任务<br>        
       * --log 日志分割<br>     
       * --mapper 对象映射<br>     
       * --request HTTP请求<br>     
       * --service 服务<br>     
       * --tools 工具<br> 
                 
   * --resources <br>
      * --browserDriver 浏览器驱动器 <br>
      * --mapper 对象映射配置文件 <br>
      * --static 静态文件(js css picture) <br>
      * --static templates(html ) <br>
      * --static application.properties 配置文件 <br>
    
      * --logback-spring.xml日志配置 <br>
       
       
   * --test <br>

#修改配置文件 application-dev.properties/application-pro.properties

#打包 mvn clean package 指定环境dev/pro
#部署 java -jar target/xxx.jar