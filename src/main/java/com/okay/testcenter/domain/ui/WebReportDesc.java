package com.okay.testcenter.domain.ui;

import lombok.Getter;
import lombok.Setter;

/**
 * @author XieYangYang
 * @date 2020/6/10 17:37
 */
@Getter
@Setter
public class WebReportDesc {
    Integer id;
    String caseName;
    String url;
    String startTime;
    String endTime;
    String shotPic;
    Character status;
    Integer webReportId;
    String errorMsg;
}
