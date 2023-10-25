package com.okay.testcenter.common.enumFile.webUi;

import java.util.LinkedHashMap;
import java.util.Map;

public enum TestOperation {
    NO_OPERATION(1,"选中元素"),
    CLICK(2, "点击"),
    INPUT(3, "输入"),
    SLIDE(4, "滑动");

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

    TestOperation(Integer value, String valueName) {
        this.value = value;
        this.valueName = valueName;
    }

    public static String getByValue(Integer value) {
        for (TestOperation testStep : TestOperation.values()) {
            if (testStep.getValue().equals(value)) {
                return testStep.getValueName();
            }
        }
        return "";
    }

    public static Integer getByValueName(String valueName) {
        for (TestOperation testStep : TestOperation.values()) {
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
        for (TestOperation testStep : TestOperation.values()) {
            map.put(testStep.getValue(), testStep.getValueName());
        }
        return map;
    }

}
