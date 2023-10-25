package com.okay.testcenter.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author zhou
 * @date 2020/12/23
 */
@Setter
@Getter
public class AlarmHistory {

    @NotNull
    private int id;
    @NotNull
    private int taskId;
    @NotNull
    private String errorType;
    @NotNull
    private String serviceName;
    @NotNull
    private String content;
    @NotNull
    private String time;
    @NotNull
    private String alarm;

}
