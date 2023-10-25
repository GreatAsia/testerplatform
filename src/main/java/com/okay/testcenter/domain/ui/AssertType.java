package com.okay.testcenter.domain.ui;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class AssertType {
    @Getter
    @Setter
    Integer id;
    @Getter
    @Setter
    @NotNull(message = "断言类型不能为空")
    @Range(min = 1,max = 20,message = "断言类型不符合要求")
    String name;
    @Getter
    @Setter
    String isDelete;
}
