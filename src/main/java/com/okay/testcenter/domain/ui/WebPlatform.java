package com.okay.testcenter.domain.ui;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class WebPlatform {

    @NotNull
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String platformName;
    @Getter
    @Setter
    private String loginUrl;
    @Getter
    @Setter
    private String loginParams;
    @Getter
    @Setter
    private Character requestType;
    @Getter
    @Setter
    private Character bodyType;
    @Getter
    @Setter
    private Character isDelete;
}
