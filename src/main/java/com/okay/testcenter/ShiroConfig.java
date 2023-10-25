package com.okay.testcenter;


import com.okay.testcenter.domain.bean.Group;
import com.okay.testcenter.service.user.GroupService;
import com.okay.testcenter.tools.Base64Utils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author XieYangYang
 * @date 2019/11/20 16:58
 */
@Configuration
@ComponentScan("com.okay.testcenter")
public class ShiroConfig {

    @Value("${redisson.address}")
    private String redisHost;

    @Value("${redisson.password}")
    private String redisPassword;


    @Value("${redission.database}")
    private int redisDB;

    @Value("${shiro.redis.timeout}")
    private int redisTimeout;


    @Autowired
    GroupService groupService;

    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();     // crazycake 实现
        redisManager.setHost(redisHost);
        if (!redisPassword.equals("") && redisPassword != null) {
            redisManager.setPassword(redisPassword);
        }
        redisManager.setDatabase(redisDB);  // 第一个库
        redisManager.setTimeout(redisTimeout);
        return redisManager;
    }

    /**
     * cookie管理对象;记住我功能
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        try {
            cookieRememberMeManager.setCipherKey(Base64Utils.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cookieRememberMeManager;
    }

    /**
     * 记住我;
     *
     * @return simpleCookie
     */
    public SimpleCookie rememberMeCookie() {
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // <!-- 记住我cookie生效时间5天 ,单位秒;-->
        simpleCookie.setMaxAge(3600 * 24 * 5);
        return simpleCookie;
    }

    /**
     * 设置cookie
     *
     * @return SimpleCookie
     */

    @Bean
    public SimpleCookie cookie() {
        SimpleCookie cookie = new SimpleCookie("SHAREJSESSIONID"); //  cookie的name,对应的默认是 JSESSIONID
        cookie.setHttpOnly(true);
        cookie.setPath("/");        //  path为 / 用于多个系统共享JSESSIONID
        return cookie;
    }


    /**
     * 配置sessionDao
     *
     * @param redisManager 注入redis相关配置
     * @return RedisSessionDAO
     */
    @Bean
    public RedisSessionDAO sessionDAO(@Qualifier("redisManager") RedisManager redisManager) {
        // crazycake 实现
        RedisSessionDAO sessionDAO = new RedisSessionDAO();
        // 设置redis
        sessionDAO.setRedisManager(redisManager);
        return sessionDAO;
    }

    /**
     * 设置SessionManager
     *
     * @param simpleCookie 注入simpleCookie
     * @param sessionDAO   注入sessionDAO
     * @return SessionManager
     */
    @Bean
    public SessionManager sessionManager(
            @Qualifier("cookie") SimpleCookie simpleCookie,
            @Qualifier("sessionDAO") RedisSessionDAO sessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(redisTimeout * 1000);    // 设置session超时
        sessionManager.setDeleteInvalidSessions(true);      // 删除无效session
        sessionManager.setSessionIdCookie(simpleCookie);            // 设置JSESSIONID
        sessionManager.setSessionDAO(sessionDAO);         // 设置sessionDAO
        sessionManager.setSessionIdUrlRewritingEnabled(false);  //去除url的JSESSIONID
        return sessionManager;
    }


    /**
     * 配置RedisCacheManager
     *
     * @param redisManager 注入redis相关配置
     * @return RedisCacheManager
     */
    @Bean
    public RedisCacheManager redisCacheManager(@Qualifier("redisManager") RedisManager redisManager) {
        RedisCacheManager cacheManager = new RedisCacheManager();   // crazycake 实现
        cacheManager.setRedisManager(redisManager);
        cacheManager.setExpire(redisTimeout);
        return cacheManager;
    }


    //不加这个注解不生效
   /* @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }*/

    //将自己的验证方式加入容器
    @Bean
    public CustomRealm myShiroRealm() {
        CustomRealm customRealm = new CustomRealm();
        return customRealm;
    }

    //权限管理，配置
    @Bean
    public SecurityManager securityManager(@Qualifier("myShiroRealm") CustomRealm myShiroRealm,
                                           @Qualifier("rememberMeManager") CookieRememberMeManager rememberMeManager,
                                           @Qualifier("sessionManager") SessionManager sessionManager,
                                           @Qualifier("redisCacheManager") RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // realm自定义数据操作
        securityManager.setRealm(myShiroRealm);
        // 注入记住我管理器;
        securityManager.setRememberMeManager(rememberMeManager());
        // session管理器
        securityManager.setSessionManager(sessionManager);
        // 缓存管理器
        securityManager.setCacheManager(redisCacheManager);
        return securityManager;
    }


    /**
     * 过滤器
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //自定义roles拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        filtersMap.put("roles", new LoginInterceptor());
        shiroFilterFactoryBean.setFilters(filtersMap);

        Map<String, String> map = new LinkedHashMap<>();
        //登出
        map.put("/logout", "logout");
        map.put("/404", "anon");
        map.put("/405", "anon");
        map.put("/css/**", "anon");
        map.put("/image/**", "anon");
        map.put("/js/**", "anon");
        map.put("/", "anon");
        map.put("/favicon.ico", "anon");

        //兼容之前
        //排除路径
        map.put("/singleLogin","anon");

        map.put("/middle/report/detail/*","anon");
        map.put("/middle/runMiddleProject","anon");
        map.put("/middle/interface/list","anon");
        map.put("/middle/runMiddleCase","anon");
        map.put("/middle/devops/run","anon");

        map.put("/dubbo/run","anon");
        map.put("/dubbo/runByModel","anon");

        map.put("/api/padautocase/run","anon");

        map.put("/performance/pad/getLastRunId","anon");
        map.put("/performance/pad/insert","anon");
        map.put("/performance/pad/inserts","anon");

        map.put("/uiPad/report/lastRunId","anon");
        map.put("/uiPad/report/insertFile","anon");
        map.put("/uiPad/report/serialnolist/insert","anon");
        map.put("/uiPad/report/caseList/insert","anon");
        map.put("/uiPad/report/update","anon");

        // job
        map.put("/job/refreshTrigger","anon");
        //log
        map.put("/websocket","anon");

        Map<String, String> groupMenuByGroups = groupService.getAllMenusAndRole(new Group());
        Set<String> groupMenuByGroupKeys = groupMenuByGroups.keySet();
        for (String key : groupMenuByGroupKeys) {
            map.put(key, "roles[" + groupMenuByGroups.get(key) + "]");
        }

        map.put("/**", "user");
        /*map.put("/api/home", "roles[admin,user_develop]");*/
        //错误页面，无权限跳转

        shiroFilterFactoryBean.setLoginUrl("/login");

        shiroFilterFactoryBean.setUnauthorizedUrl("/405");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }


}
