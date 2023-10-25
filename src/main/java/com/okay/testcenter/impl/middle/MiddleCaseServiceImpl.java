package com.okay.testcenter.impl.middle;


import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.Feature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.middle.*;
import com.okay.testcenter.mapper.middle.MiddleCaseMapper;
import com.okay.testcenter.mapper.middle.MiddleInterfaceMapper;
import com.okay.testcenter.mapper.middle.MiddleModuleMapper;
import com.okay.testcenter.mapper.middle.MiddleProjectMapper;
import com.okay.testcenter.service.middle.MiddleCaseService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.params.Params;
import sun.security.pkcs11.Secmod;

import javax.annotation.Resource;
import javax.swing.text.html.parser.Entity;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


@Service("MiddleCaseService")
public class MiddleCaseServiceImpl implements MiddleCaseService {

    @Resource
    MiddleCaseMapper middleCaseMapper;
    @Resource
    MiddleModuleMapper middleModuleMapper;
    @Resource
    MiddleInterfaceMapper middleInterfaceMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertMiddleCase(MiddleCase middleCase) {

        middleCaseMapper.insertMiddleCase(middleCase);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMiddleCase(int id) {
        middleCaseMapper.deleteMiddleCase(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMiddleCase(MiddleCase middleCase) {
        middleCaseMapper.updateMiddleCase(middleCase);
    }

    @Override
    public MiddleCase findMiddleCaseById(int id) {
        return middleCaseMapper.findMiddleCaseById(id);
    }


    @Override
    public List<MiddleCase> findMiddleCaseByName(String name) {
        return middleCaseMapper.findMiddleCaseByName(name);
    }

    @Override
    public List<MiddleCase> findMiddleCaseByPath(String path) {
        return middleCaseMapper.findMiddleCaseByPath(path);
    }

    @Override
    public PageInfo findMiddleCaseList(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<MiddleCase> middleCaseList = middleCaseMapper.findMiddleCaseList();
        PageInfo pageInfo = new PageInfo(middleCaseList);
        return pageInfo;
    }

    @Override
    public PageInfo findMiddleCaseByEnvAndInterface(int env_id, int interface_id, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<MiddleCase> middleCaseList = middleCaseMapper.findMiddleCaseByEnvAndInterface(env_id, interface_id);
        PageInfo pageInfo = new PageInfo(middleCaseList);
        return pageInfo;
    }

    @Override
    public List<MiddleCase> findMiddleCaseByEnvAndInterface(int env_id, int interface_id) {
        List<MiddleCase> middleCaseList = middleCaseMapper.findMiddleCaseByEnvAndInterface(env_id, interface_id);
        return middleCaseList;
    }

    @Override
    public List<MiddleCase> findMiddleCaseByEnvId(int envId) {
        return middleCaseMapper.findMiddleCaseByEnvId(envId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncMiddleCase(int fromEnv, int toEnv) {

        List<MiddleCase> fromMiddleList = middleCaseMapper.findMiddleCaseByEnvId(fromEnv);
        List<MiddleCase> toMiddleList = middleCaseMapper.findMiddleCaseByEnvId(toEnv);

        if (fromMiddleList != null && !fromMiddleList.isEmpty()) {

            Map<String, String> dataMap = new HashMap<String, String>();
            for (MiddleCase t : toMiddleList) {
                String name = t.getName().trim();
                dataMap.put(name, name);
            }

            List<MiddleCase> middleCaseList = new ArrayList<>();
            for (MiddleCase f : fromMiddleList) {
                String caseName = f.getName().trim();
                if (!dataMap.containsKey(caseName)) {
                    middleCaseList.add(f);
                }
            }

            for (MiddleCase m : middleCaseList) {
                logger.info("同步用例==" + JSONObject.toJSONString(m));
                m.setId(0);
                m.setEnv_id(toEnv);
                middleCaseMapper.insertMiddleCase(m);
            }
            logger.info("同步用例 " + middleCaseList.size() + " 条完成!!!");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncMiddleCaseByProject(int projectId, int fromEnv, int toEnv) {

        List<MiddleInterface> middleInterfaceList = new ArrayList<>();
        List<MiddleCase> fromMiddleList = new ArrayList<>();
        List<MiddleCase> toMiddleList = new ArrayList<>();

        List<MiddleModule> modules = middleModuleMapper.findMiddleModuleByProjectId(projectId);
        for (MiddleModule m : modules) {
            List<MiddleInterface> middleInterfaces = middleInterfaceMapper.findMiddleInterfaceByModuleId(m.getId());
            middleInterfaceList.addAll(middleInterfaces);
        }

        for (MiddleInterface m : middleInterfaceList) {
            List<MiddleCase> fromMiddles = middleCaseMapper.findMiddleCaseByEnvAndInterface(fromEnv, m.getId());
            fromMiddleList.addAll(fromMiddles);
        }
        for (MiddleInterface m : middleInterfaceList) {
            List<MiddleCase> fromMiddles = middleCaseMapper.findMiddleCaseByEnvAndInterface(toEnv, m.getId());
            toMiddleList.addAll(fromMiddles);
        }

        if (fromMiddleList != null && !fromMiddleList.isEmpty()) {

            Map<String, String> dataMap = new HashMap<String, String>();
            for (MiddleCase t : toMiddleList) {
                String name = t.getName().trim();
                dataMap.put(name, name);
            }

            List<MiddleCase> middleCaseList = new ArrayList<>();
            for (MiddleCase f : fromMiddleList) {
                String caseName = f.getName().trim();
                if (!dataMap.containsKey(caseName)) {
                    middleCaseList.add(f);
                }
            }

            for (MiddleCase m : middleCaseList) {
                logger.info("同步用例==" + JSONObject.toJSONString(m));
                m.setId(0);
                m.setEnv_id(toEnv);
                middleCaseMapper.insertMiddleCase(m);
            }
            logger.info("同步用例 " + middleCaseList.size() + " 条完成!!!");
        }
    }

    @Override
    public List<MiddleCaseRule> getMiddleCaseRule(String params, String requestType) {
        List<MiddleCaseRule> middleCaseRules = new ArrayList<>();
        switch (requestType) {
            case "Post-Json":
                analysisPostJson(params, middleCaseRules, "");
                break;
            default:
                middleCaseRules = analysisGetParamOrPostForm(params);
        }
        return middleCaseRules;
    }


    /**
     * 解析get请求参数
     *
     * @param params
     * @return
     */
    private List<MiddleCaseRule> analysisGetParamOrPostForm(String params) {
        String[] paramsArray = params.split("&");
        List<MiddleCaseRule> middleCaseRules = new ArrayList<>();
        for (String param : paramsArray) {
            if (param != null && !param.equals("")) {
                MiddleCaseRule middleCaseRule = new MiddleCaseRule();
                if (!param.contains("=")) { //不符合规则跳出本次循环，开始下次循环
                    continue;
                }
                String[] keyAndValue = param.split("=");
                middleCaseRule.setFieldName(keyAndValue[0]);
                //TODO value为json
                //当value为json的时候
                if (StringUtils.isNumeric(keyAndValue[1])) {
                    middleCaseRule.setFieldType(1); //设置为int类型
                } else if (isJSonArray(keyAndValue[1])) {
                    middleCaseRule.setFieldType(3); //设置array
                } else if (isJSon(keyAndValue[1])) {
                    middleCaseRule.setFieldType(2); //设置array
                } else {
                    middleCaseRule.setFieldType(0); //设置string类型
                }
                middleCaseRules.add(middleCaseRule);
            }
        }
        return middleCaseRules;
    }


    /**
     * 解析json
     *
     * @param params
     * @return
     */
    public void analysisPostJson(String params, List<MiddleCaseRule> middleCaseRules, String parentKey) {
        if (isJSonArray(params)) {
            JSONArray jsonArray = JSON.parseArray(params);
            if (jsonArray.size() == 0) {
                return;
            }
            //判断子元素是不是json
            if (!isJSon(String.valueOf(jsonArray.get(0)))) {
                return;
            }
            //由于要去除多层 所以需要取第一个
            //for (Object o : jsonArray) {
            analysisJson(String.valueOf(jsonArray.get(0)), middleCaseRules, parentKey);
            //}
        } else {
            analysisJson(params, middleCaseRules, parentKey);
        }
    }

    private void analysisJson(String params, List<MiddleCaseRule> middleCaseRules, String parentKey) {
        LinkedHashMap<String, Object> map = JSONObject.parseObject(params, new TypeReference<LinkedHashMap<String, Object>>() {
        }, Feature.OrderedField);
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            MiddleCaseRule middleCaseRule = new MiddleCaseRule();
            Map.Entry<String, Object> next = iterator.next();
            middleCaseRule.setFieldName(parentKey + next.getKey());

            if (next.getValue() instanceof JSON) {
                middleCaseRule.setFieldType(2);
            } else if (next.getValue() instanceof JSONArray) {
                middleCaseRule.setFieldType(3);
            } else if (StringUtils.isNumeric(String.valueOf(next.getValue()))) {
                middleCaseRule.setFieldType(1);
            } else {
                middleCaseRule.setFieldType(0);
            }
            middleCaseRules.add(middleCaseRule);
            if (next.getValue() instanceof JSON || next.getValue() instanceof JSONArray) {
                String oldParentKey = parentKey;
                parentKey += (next.getKey() + "-");
                analysisPostJson(String.valueOf(next.getValue()), middleCaseRules, parentKey); //递归
                parentKey = oldParentKey; //重置前缀
            }
        }
    }

    /**
     * 判断是不是jsonArray
     *
     * @param params
     * @return
     */
    private Boolean isJSonArray(String params) {
        boolean isJSONArray = false;
        try {
            Object o = JSON.parseArray(params);
            if (o instanceof JSONArray) {
                isJSONArray = true;
            }
        } catch (JSONException e) {
        }
        return isJSONArray;
    }

    private Boolean isJSon(String params) {
        boolean isJSON = false;
        try {
            JSON.parseObject(params);
            isJSON = true;
        } catch (JSONException e) {
        }
        return isJSON;
    }


    /**
     * 生成测试用例
     *
     * @param middleCaseRules
     * @param params
     * @return
     */
    public List<MiddleCase> genMiddleCase(List<MiddleCaseRule> middleCaseRules, String params, String requestMethod, MiddleCase oldMiddleCase) {
        List<MiddleCase> MiddleCase = new ArrayList<>();
        switch (requestMethod) {
            case "Post-Json":
                try {
                    MiddleCase = genJSONCase(middleCaseRules, params, oldMiddleCase);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    MiddleCase = genFormCase(middleCaseRules, params, oldMiddleCase);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;

        }
        //清楚之前数据库记录
        middleCaseMapper.deleteMiddleCasesByRefId(oldMiddleCase.getId());
        //数据库写入
        remDuplicateCaseAndInset(MiddleCase);
        return MiddleCase;
    }


    public void remDuplicateCaseAndInset(List<MiddleCase> middleCases) {
        //案例去重
        List<String> caseParam = new ArrayList<>();
        List<Integer> caseIndexs = new ArrayList<>();
        for (int index = 0; index < middleCases.size(); index++) {
            MiddleCase middleCase = middleCases.get(index);
            if (caseParam.contains(middleCase.getRequest_data())) {
                caseIndexs.add(index);
            }
            caseParam.add(middleCase.getRequest_data());
        }
        for (int caseIndex : caseIndexs) {
            middleCases.remove(caseIndex);
        }


        if (middleCases.size() > 0) {
            middleCaseMapper.insertMiddleCases(middleCases);
        }
    }


    /**
     * 生成formcase
     *
     * @param middleCaseRules
     * @param params
     * @param oldMiddleCase
     * @return
     */
    public List<MiddleCase> genFormCase(List<MiddleCaseRule> middleCaseRules, String params, MiddleCase oldMiddleCase) throws InvocationTargetException, IllegalAccessException {
        List<MiddleCase> list = new ArrayList<>();
        for (MiddleCaseRule middleCaseRule : middleCaseRules) {
            String fileds = middleCaseRule.getFieldName();
            Integer fieldType = middleCaseRule.getFieldType();
            String range = middleCaseRule.getRange();
            Integer required = middleCaseRule.getRequired();
            //必填项
            if (required == 1) {
                //设置为空字符串
                list.add(setFormCase(oldMiddleCase, params, fileds, "", "设置为空字符串"));
                //无该字段
                list.add(setFormCase(oldMiddleCase, params, fileds, null, "无该字段"));
            }
            switch (fieldType) {
                case 0:
                    if (range != null && range.contains("-")) {
                        String[] ranges = range.split("-");
                        if (ranges.length != 2) {
                            break;
                        }
                        if (StringUtils.isNumeric(ranges[0]) && Integer.valueOf(ranges[0]) > 0) {
                            // 设置参数最小长度-1
                            String filedParam = RandomStringUtils.random(Integer.valueOf(ranges[0]) - 1, "abcdefgABCDEFG");
                            list.add(setFormCase(oldMiddleCase, params, fileds, filedParam, "设置参数为'" + filedParam + "'"));
                            // 设置参数最小长度
                            String filedParamMin = RandomStringUtils.random(Integer.valueOf(ranges[0]), "abcdefgABCDEFG");
                            list.add(setFormCase(oldMiddleCase, params, fileds, filedParamMin, "设置参数为'" + filedParamMin + "'"));
                        }
                        //中间参数
                        if (StringUtils.isNumeric(ranges[0]) && StringUtils.isNumeric(ranges[1])) {
                            int mid = (int) Math.round((double) (Integer.valueOf(ranges[0]) + Integer.valueOf(ranges[1])) / 2);
                            if (mid != Integer.valueOf(ranges[0])&&mid != Integer.valueOf(ranges[1])) {
                                String midParam = RandomStringUtils.random(mid, "abcdefgABCDEFG");
                                list.add(setFormCase(oldMiddleCase, params, fileds, midParam, "设置参数为'" + midParam + "'"));
                            }

                        }
                        if (StringUtils.isNumeric(ranges[1])) {
                            //最大长度
                            String filedParamMax = RandomStringUtils.random(Integer.valueOf(ranges[1]), "abcdefgABCDEFG");
                            list.add(setFormCase(oldMiddleCase, params, fileds, filedParamMax, "设置参数为'" + filedParamMax + "'"));
                            //最大长度+1
                            String filedParam = RandomStringUtils.random(Integer.valueOf(ranges[1]) + 1, "abcdefgABCDEFG");
                            list.add(setFormCase(oldMiddleCase, params, fileds, filedParam, "设置参数为'" + filedParam + "'"));
                        }
                    }
                    break;
                case 1: //int
                    if (range != null&&!range.equals("")) {
                        String[] ranges = range.split("-");
                        if (ranges.length != 2) {
                            break;
                        }
                        if (StringUtils.isNumeric(ranges[0])) {
                            //设置参数最小-1
                            String filedParam = (Integer.valueOf(ranges[0]) - 1) + "";
                            list.add(setFormCase(oldMiddleCase, params, fileds, filedParam, "设置设置参数为" + filedParam));
                            //设置参数最小
                            String filedParamMin = (Integer.valueOf(ranges[0])) + "";
                            list.add(setFormCase(oldMiddleCase, params, fileds, filedParamMin, "设置设置参数为" + filedParamMin));
                        }
                        // 中间参数
                        if (StringUtils.isNumeric(ranges[0]) && StringUtils.isNumeric(ranges[1])) {
                            int mid = (int) Math.round((double) (Integer.valueOf(ranges[0]) + Integer.valueOf(ranges[1])) / 2);
                            if (mid != Integer.valueOf(ranges[0]) && mid != Integer.valueOf(ranges[1])) {
                                String midParam = String.valueOf(mid);
                                list.add(setFormCase(oldMiddleCase, params, fileds, midParam, "设置设置参数为" + midParam));
                            }
                        }


                        if (StringUtils.isNumeric(ranges[1])) {
                            //设置参数最大
                            String filedParamMax = (Integer.valueOf(ranges[1])) + "";
                            list.add(setFormCase(oldMiddleCase, params, fileds, filedParamMax, "设置设置参数为" + filedParamMax));
                            //设置参数最大+1
                            String filedParam = (Integer.valueOf(ranges[1]) + 1) + "";
                            list.add(setFormCase(oldMiddleCase, params, fileds, filedParam, "设置设置参数为" + filedParam));
                        }

                    }
                    //设置类型XXX(String)
                    list.add(setFormCase(oldMiddleCase, params, fileds, "XXX", "设置设置参数为XXX"));
                    break;
                case 2: //node节点
                    // TODO 暂时不做
                    break;
                case 3: //Array
                    // TODO 暂时不做生成
                default:
                    // 兜底什么都不生成
                    break;
            }
        }
        return list;
    }


    /**
     * 生成新form参数
     *
     * @param params
     * @param newValue
     * @param filed
     * @return
     */
    public String genFormParam(String params, String newValue, String filed) {
        String newParams = "";
        if (params != null && !params.equals("")) {
            String[] paramArray = params.split("&");
            for (String param : paramArray) {

                String[] paramKeyAndValue = param.split("=");
                String key = paramKeyAndValue[0];
                String value = paramKeyAndValue[1];
                if (key.equals(filed) && newValue == null) {
                    continue;
                }
                if (key.equals(filed)) {
                    value = newValue;
                }
                if (newParams.equals("")) {
                    newParams += key + "=" + value;
                } else {
                    newParams += "&" + key + "=" + value;
                }
            }
        }
        return newParams;
    }


    /**
     * 设置formcase参数
     *
     * @param oldMiddleCase
     * @param params
     * @param fileds
     * @param newValue
     * @param newCaseName
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public MiddleCase setFormCase(MiddleCase oldMiddleCase, String params, String fileds, String newValue, String newCaseName) throws InvocationTargetException, IllegalAccessException {
        MiddleCase newMiddleCase = new MiddleCase();
        BeanUtils.copyProperties(newMiddleCase, oldMiddleCase);
        String newParams = genFormParam(params, newValue, fileds);
        newMiddleCase.setRequest_data(newParams);
        newMiddleCase.setName(newMiddleCase.getName() + ":" + fileds + newCaseName);
        newMiddleCase.setId(0);
        newMiddleCase.setCaseType(1);
        newMiddleCase.setAuto(1);
        newMiddleCase.setRef_id(oldMiddleCase.getId());
        return newMiddleCase;
    }


    /**
     * 生成MiddleCase
     *
     * @param middleCaseRules
     * @param params
     * @return
     */
    public List<MiddleCase> genJSONCase(List<MiddleCaseRule> middleCaseRules, String params, MiddleCase oldMiddleCase) throws InvocationTargetException, IllegalAccessException {
        List<MiddleCase> list = new ArrayList<>();
        for (MiddleCaseRule middleCaseRule : middleCaseRules) {
            String fileds = middleCaseRule.getFieldName();
            Integer fieldType = middleCaseRule.getFieldType();
            String range = middleCaseRule.getRange();
            Integer required = middleCaseRule.getRequired();
            //必填项
            if (required == 1) {
                //设置为空字符串
                list.add(setCase(oldMiddleCase, params, fileds, "", "设置为空字符串"));
                //无该字段
                list.add(setCase(oldMiddleCase, params, fileds, null, "无该字段"));
            }
            switch (fieldType) {
                case 0:
                    if (range != null && range.contains("-")) {
                        String[] ranges = range.split("-");
                        if (ranges.length != 2) {
                            break;
                        }
                        if (StringUtils.isNumeric(ranges[0]) && Integer.valueOf(ranges[0]) > 0) {
                            // 设置参数最小长度
                            String filedParamMin = RandomStringUtils.random(Integer.valueOf(ranges[0]), "abcdefgABCDEFG");
                            list.add(setCase(oldMiddleCase, params, fileds, filedParamMin, "设置参数为'" + filedParamMin + "'"));

                            String filedParam = RandomStringUtils.random(Integer.valueOf(ranges[0]) - 1, "abcdefgABCDEFG");
                            list.add(setCase(oldMiddleCase, params, fileds, filedParam, "设置参数为'" + filedParam + "'"));
                        }

                        //中间参数
                        if (StringUtils.isNumeric(ranges[0]) && StringUtils.isNumeric(ranges[1])) {
                            int mid = (int) Math.round((double) (Integer.valueOf(ranges[0]) + Integer.valueOf(ranges[1])) / 2);
                            if (mid != Integer.valueOf(ranges[0])&&mid != Integer.valueOf(ranges[1])) {
                                String midParam = RandomStringUtils.random(mid, "abcdefgABCDEFG");
                                list.add(setCase(oldMiddleCase, params, fileds, midParam, "设置参数为'" + midParam + "'"));
                            }

                        }
                        if (StringUtils.isNumeric(ranges[1])) {
                            //最大长度
                            String filedParamMax = RandomStringUtils.random(Integer.valueOf(ranges[1]), "abcdefgABCDEFG");
                            list.add(setCase(oldMiddleCase, params, fileds, filedParamMax, "设置参数为'" + filedParamMax + "'"));

                            String filedParam = RandomStringUtils.random(Integer.valueOf(ranges[1]) + 1, "abcdefgABCDEFG");
                            list.add(setCase(oldMiddleCase, params, fileds, filedParam, "设置参数为'" + filedParam + "'"));
                        }
                    }
                    break;
                case 1: //int
                    if (range != null && range.contains("-")) {
                        String[] ranges = range.split("-");
                        if (ranges.length != 2) {
                            break;
                        }
                       /* if (StringUtils.isNumeric(ranges[0])) {
                            String filedParam = (Integer.valueOf(ranges[0]) - 1) + "";
                            list.add(setCase(oldMiddleCase, params, fileds, filedParam, "设置设置参数为" + filedParam));
                        }
                        if (StringUtils.isNumeric(ranges[1])) {
                            String filedParam = (Integer.valueOf(ranges[1]) + 1) + "";
                            list.add(setCase(oldMiddleCase, params, fileds, filedParam, "设置设置参数为" + filedParam));
                        }*/


                        if (StringUtils.isNumeric(ranges[0])) {
                            //设置参数最小-1
                            String filedParam = (Integer.valueOf(ranges[0]) - 1) + "";
                            list.add(setCase(oldMiddleCase, params, fileds, filedParam, "设置设置参数为" + filedParam));
                            //设置参数最小
                            String filedParamMin = (Integer.valueOf(ranges[0])) + "";
                            list.add(setCase(oldMiddleCase, params, fileds, filedParamMin, "设置设置参数为" + filedParamMin));
                        }
                        // 中间参数
                        if (StringUtils.isNumeric(ranges[0]) && StringUtils.isNumeric(ranges[1])) {
                            int mid = (int) Math.round((double) (Integer.valueOf(ranges[0]) + Integer.valueOf(ranges[1])) / 2);
                            if (mid != Integer.valueOf(ranges[0]) && mid != Integer.valueOf(ranges[1])) {
                                String midParam = String.valueOf(mid);
                                list.add(setCase(oldMiddleCase, params, fileds, midParam, "设置设置参数为" + midParam));
                            }
                        }


                        if (StringUtils.isNumeric(ranges[1])) {
                            //设置参数最大
                            String filedParamMax = (Integer.valueOf(ranges[1])) + "";
                            list.add(setCase(oldMiddleCase, params, fileds, filedParamMax, "设置设置参数为" + filedParamMax));
                            //设置参数最大+1
                            String filedParam = (Integer.valueOf(ranges[1]) + 1) + "";
                            list.add(setCase(oldMiddleCase, params, fileds, filedParam, "设置设置参数为" + filedParam));
                        }

                    }
                    //设置类型XXX(String)
                    list.add(setCase(oldMiddleCase, params, fileds, "XXX", "设置设置参数为XXX"));
                    break;
                case 2: //node节点
                    // TODO 暂时不做
                    break;
                case 3: //Array
                    // TODO 暂时不做生成
                default:
                    // 兜底什么都不生成
                    break;
            }
        }
        return list;
    }

    /**
     * 设置case参数
     *
     * @param oldMiddleCase
     * @param params
     * @param fileds
     * @param newValue
     * @param newCaseName
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public MiddleCase setCase(MiddleCase oldMiddleCase, String params, String fileds, String newValue, String newCaseName) throws InvocationTargetException, IllegalAccessException {
        MiddleCase newMiddleCase = new MiddleCase();
        BeanUtils.copyProperties(newMiddleCase, oldMiddleCase);
        String newParams = genJSON(params, fileds, newValue);
        newMiddleCase.setRequest_data(newParams);
        newMiddleCase.setName(newMiddleCase.getName() + ":" + fileds + newCaseName);
        newMiddleCase.setId(0);
        newMiddleCase.setCaseType(1);
        newMiddleCase.setAuto(1);
        newMiddleCase.setRef_id(oldMiddleCase.getId());
        return newMiddleCase;
    }

    /**
     * 生成新的json
     *
     * @param params
     * @param fileds
     * @param newValue
     * @return
     */
    private String genJSON(String params, String fileds, String newValue) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        if (isJSonArray(params)) {
            genJSON(JSONArray.parseArray(params).get(0).toString(), fileds, newValue);
        } else {
            String[] filedArray = fileds.split("-");
            for (int i = 0; i < filedArray.length; i++) {
                String value = jsonObject.getString(filedArray[i]);
                if (i == filedArray.length - 1) {
                    if (newValue == null) {
                        jsonObject.remove(filedArray[i]);
                    } else {
                        jsonObject.put(filedArray[i], newValue);
                    }
                } else if (isJSonArray(value)) {
                    genJSON(JSONArray.parseArray(value).get(0).toString(), fileds.replace(filedArray[i] + "-", ""), newValue);
                } else if (isJSon(value)) {
                    genJSON(JSONArray.parseArray(value).get(0).toString(), fileds.replace(filedArray[i] + "-", ""), newValue);
                }
            }

        }
        return jsonObject.toJSONString();
    }


}
