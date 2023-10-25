package com.okay.testcenter.common.aop;

import com.github.pagehelper.PageHelper;
import com.okay.testcenter.domain.Page;
import org.apache.ibatis.javassist.*;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xieyangyang on 2020/3/5.
 */
@Aspect
@Component
public class PageHelperAop {
    private static final Logger log = LoggerFactory.getLogger(PageHelperAop.class);

    private static final ThreadLocal<Page> pageContext = new ThreadLocal<>();

    //以WithPage结尾的Controller方法都是需要分页的方法
    //@Before(value = "execution(* com.web.service.imp..*.*Page(..))")
    @Before(value = "execution(* com.okay.testcenter.controller..*.*Page(..))")
    public void controllerAop(JoinPoint joinPoint) throws Exception {
        log.info("执行PageHelperAop开始");
        Page page = null;

        Object[] args = joinPoint.getArgs();

        //获取类名
        String clazzName = joinPoint.getTarget().getClass().getName();
        //获取方法名称
        String methodName = joinPoint.getSignature().getName();
        //该方法通过反射获取参数列表
        Map<String, Object> nameAndArgs = this.getFieldsName(this.getClass(), clazzName, methodName, args);

        page = (Page) nameAndArgs.get("page");

        int currentPageOld = 1;
        int size = 5;
        if (null == page) {
            page.setCurrentPage(currentPageOld);
            page.setPageSize(size);
        }else{
            if(page.getCurrentPage()<1){
                page.setCurrentPage(currentPageOld);
            }
            if(page.getPageSize()<1){
                page.setPageSize(size);
            }
        }

        //将分页参数放置线程变量中
        pageContext.set(page);
        PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
        log.info(String.format("分页当前页面为%s,分页大小为%s", page.getCurrentPage(), page.getPageSize()));
        log.info("执行PageHelperAop结束");
    }

    /*@Before(value = "execution(* com.web.controller..*.*Page(..))")
    public void daoAop() throws Exception {
        Page page = pageContext.get();
        PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
    }*/

    private Map<String, Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            map.put(attr.variableName(i + pos), args[i]);
        }
        return map;
    }


    public String getJsonValue(String jsonObject, String searchKey) {
        //"json":(.*?),
        String pt = searchKey;
        Pattern pattern = Pattern.compile(pt);
        Matcher matcher = pattern.matcher(jsonObject);
        if (matcher.find()) {
            return matcher.group(1);
        }


        return null;
    }

}
