package com.okay.testcenter.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zhou
 * @date 2020/12/23
 */
@Setter
@Getter
public class AlarmPeople {

    @NotNull
    private int id;
    @NotNull
    private String project;
    @NotNull
    private String phone;

    private Date create_time;

    private Date update_time;

    private String rd_name;
    private String qa_name;


}
