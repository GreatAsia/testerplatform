package com.okay.testcenter.domain.ui;

import lombok.Getter;
import lombok.Setter;

/**
 * @author XieYangYang
 * @date 2020/6/10 17:34
 */
@Getter
@Setter
public class WebReport {
    Integer id;
    String projectNames;
    String projectIds;
    String startTime;
    String endTime;
    Integer failCaseCount;
    Integer allCaseCount;
}
