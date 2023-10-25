package com.okay.testcenter.domain.ui;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

public class WebCase {


    public interface AddGroup extends Default {
    }

    public interface SelectGroup extends Default {
    }

    @Getter
    @Setter
    private int id;

    @Getter
    @Setter
    @NotNull(message = "项目名不能为空", groups = {AddGroup.class})
    @Min(value = 1, message = "项目名不符合要求" ,groups = {AddGroup.class})
    private Integer platformId;

    @Getter
    @Setter
    private WebPlatform webPlatform;

    @Getter
    @Setter
    @NotNull(message = "url不能为空", groups = {AddGroup.class})
    /*@NotBlank(message = "url不能为空")*/
    private String url;

    @Getter
    @Setter
    @NotNull(message = "案例描述不能为空", groups = {AddGroup.class})
    /*@NotBlank(message = "案例描述不能为空")*/
    private String caseDesc;

    @Getter
    @Setter
    private String screenshot;

    @Getter
    @Setter
    @NotNull(message = "案例步骤不能为空", groups = {AddGroup.class})
    /*@NotBlank(message = "案例步骤不能为空")*/
    private String caseStep;

    @Getter
    @Setter
    private Character isDelete = 0;

    @Getter
    @Setter
    private Character testStatus;


    @Getter
    @Setter
    @NotNull(message = "是否登录不能为空", groups = {AddGroup.class})
    private Character needLogin;
}
