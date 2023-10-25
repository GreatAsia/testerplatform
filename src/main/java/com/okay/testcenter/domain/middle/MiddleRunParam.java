package com.okay.testcenter.domain.middle;


import lombok.Getter;
import lombok.Setter;

/*
  运行用例所需的参数
 */
@Getter
@Setter
public class MiddleRunParam {


    private int id;
    private String type;
    private int envId;
    private int caseType;


}
