package com.okay.testcenter.domain.ui;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class AssertTypeName {
    @Getter
    @Setter
    Integer id;
    @Getter
    @Setter
    @NotNull(message = "断言名称不能为空")
    @Range(min = 1,max = 20,message = "断言名称不符合要求")
    String name;
    @Getter
    @Setter
    Integer assertTypeId;
    @Getter
    @Setter
    String isDelete;
}
