package com.okay.testcenter.domain.middle;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zhou
 * @date 2020/11/4
 */
@Getter
@Setter
public class BugInfo {

    private int id;

    @NotNull(message = "产品ID不能为空")
    @Min(1)
    private int product;

    @NotNull(message = "标题不能为空")
    @NotBlank(message = "标题不能为空")
    private String title;

    @NotNull(message = "影响版本不能为空")
    @Min(1)
    private int openedBuild;
    //分支/平台
    private int branch;
    //所属模块
    private int module;
    //所属项目
    private int project;
    //指派给
    private String assignedTo;
    //截止日期 日期格式：YY-mm-dd，如：2019-01-01
    private Date deadline;
    //Bug类型 取值范围： | codeerror | interface | config | install | security | performance | standard | automation | others | designchange | newfeature | designdefect | trackthings
    private String type;
    //操作系统 取值范围： | all | windows | win8 | win7 | vista | winxp | win2012 | win2008 | win2003 | win2000 | android | ios | wp8 | wp7 | symbian | linux | freebsd | osx | unix | others
    private String os;
    //浏览器 取值范围： | all | ie | ie11 | ie10 | ie9 | ie8 | ie7 | ie6 | chrome | firefox | firefox4 | firefox3 | firefox2 | opera | oprea11 | oprea10 | opera9 | safari | maxthon | uc | other
    private String browser;
    //颜色格式：#RGB，如：#3da7f5
    private String color;
    //严重程度 取值范围：3 | 1 | 2 | 4 | 5
    private int severity;
    //优先级 取值范围：0 | 1 | 2 | 3 | 4
    private int pri;
    //重现步骤
    private String steps;
    //抄送给 填写帐号，多个账号用','分隔。
    private String mailto;
    //关键词
    private String keywords;

    //用户名
    private String uname;
    //密码
    private String pwd;
    //请求参数
    private String requestInfo;
    //响应参数
    private String responseInfo;
    //requestId
    private String requestId;
    //验证信息
    private String verifyInfo;



}
