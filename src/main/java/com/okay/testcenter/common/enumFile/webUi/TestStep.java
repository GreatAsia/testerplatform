package com.okay.testcenter.common.enumFile.webUi;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

public enum TestStep {
    ID(1, "id"),
    NAME(2, "name"),
    CLASSNAME(3, "class"),
    CSSSECECTOR(4, "selector"),
    XPATH(5, "xpath"),
    LINKTEXT(6, "linkText");

    private Integer value;
    /**
     * msg
     */

    private String valueName;

    public Integer getValue() {
        return value;
    }

    public String getValueName() {
        return valueName;
    }

    TestStep(Integer value, String valueName) {
        this.value = value;
        this.valueName = valueName;
    }

    public static String getByValue(Integer value) {
        for (TestStep testStep : TestStep.values()) {
            if (testStep.getValue().equals(value)) {
                return testStep.getValueName();
            }
        }
        return "";
    }

    public static Integer getByValueName(String valueName) {
        for (TestStep testStep : TestStep.values()) {
            if (testStep.getValueName().equals(valueName)) {
                return testStep.getValue();
            }
        }
        return null;
    }

    /**
     * enum 转MAP
     *
     * @return
     */
    public static Map enumToList() {
        Map<Integer, String> map = new LinkedHashMap<>();
        for (TestStep testStep : TestStep.values()) {
            map.put(testStep.getValue(), testStep.getValueName());
        }
        return map;
    }

}
