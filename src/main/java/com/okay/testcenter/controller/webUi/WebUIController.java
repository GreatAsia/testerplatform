package com.okay.testcenter.controller.webUi;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.common.enumFile.webUi.TestOperation;
import com.okay.testcenter.common.enumFile.webUi.TestStep;
import com.okay.testcenter.domain.Page;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.ui.AssertType;
import com.okay.testcenter.domain.ui.AssertTypeName;
import com.okay.testcenter.domain.ui.WebCase;
import com.okay.testcenter.domain.ui.WebPlatform;
import com.okay.testcenter.exception.myException.DataException;
import com.okay.testcenter.service.webUi.ExecuteWebUiCaseService;
import com.okay.testcenter.service.webUi.WebUIService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "UI自动化")
@Controller
@RequestMapping(value = "/ui")
public class WebUIController {

    @Autowired
    WebUIService webUIService;

    @Autowired
    ExecuteWebUiCaseService executeWebUiCaseService;


    Logger logger = LoggerFactory.getLogger(WebUIController.class);

    /**
     * 案例列表页面
     *
     * @param model
     * @param page
     * @return
     */
    @GetMapping("/webUiCaseList")
    public String getCaseListByPage(Model model, Page page, @Validated(WebCase.SelectGroup.class) WebCase webCase) {
        List<WebCase> webCases = webUIService.getListWebCase(webCase);
        PageInfo<WebCase> pageInfo = new PageInfo<>(webCases);
        logger.info(JSONObject.toJSONString(pageInfo));
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("params", webCase);
        model.addAttribute("platforms", webUIService.getPlatformById(null));
        return "webUI/WebUiCaseList";
    }

    /**
     * 添加案例页面
     *
     * @return
     */
    @GetMapping("/addUiCase")
    public String addUiCase() {
        return "webUI/webUiCaseAdd";
    }


    /**
     * 添加案例接口
     *
     * @param webCase
     * @return
     */
    @ApiOperation(value = "添加案例接口", notes = "添加案例接口")
    @ResponseBody
    @RequestMapping(value = "/addWebUiCase", method = RequestMethod.POST)
    public RetResult<Object> addCase(@Validated(WebCase.AddGroup.class) WebCase webCase) {
        boolean isInsert = webUIService.addWebCase(webCase);
        RetResult retResult = new RetResult<>();
        if (isInsert) {
            retResult.setCode(200);
            retResult.setMsg("添加成功");
            return retResult;
        }
        retResult.setCode(400);
        retResult.setMsg("添加失败");
        return retResult;
    }


    /**
     * 获取所有项目信息
     *
     * @return
     */
    @ApiOperation(value = "获取所有项目信息", notes = "获取所有项目信息")
    @GetMapping("getPlatformById")
    @ResponseBody
    public RetResult<Object> getPlatformById(Integer id) {
        List list = webUIService.getPlatformById(id);
        RetResult retResult = new RetResult();
        retResult.setData(list);
        retResult.setCode(200);
        retResult.setMsg("查询成功");
        return retResult;
    }


    /**
     * 获取所有step（key，value）
     *
     * @return
     */
    @ApiOperation(value = "获取所有step", notes = "获取所有step")
    @GetMapping("getStep")
    @ResponseBody
    public RetResult<Object> getStep() {
        RetResult retResult = new RetResult();
        retResult.setData(TestStep.enumToList());
        retResult.setCode(200);
        retResult.setMsg("查询成功");
        return retResult;
    }


    /**
     * 获取所有operation（key，value）
     *
     * @return
     */
    @ApiOperation(value = "获取所有operation", notes = "获取所有operation")
    @GetMapping("getOperation")
    @ResponseBody
    public RetResult<Object> getOperation() {
        RetResult retResult = new RetResult();
        retResult.setData(TestOperation.enumToList());
        retResult.setCode(200);
        retResult.setMsg("查询成功");
        return retResult;
    }


    /**
     * 查询所有断言类型
     *
     * @param assertType
     * @return
     */
    @ResponseBody
    @ApiOperation(value = "查询所有断言类型", notes = "查询所有断言类型")
    @RequestMapping(value = "/getAssertType", method = RequestMethod.POST)
    public RetResult<Object> getAssertType(AssertType assertType) {
        List<AssertType> assertTypes = webUIService.getAssertType(assertType);
        //PageInfo<AssertType> pageInfo = new PageInfo<>(assertTypes);
        RetResult retResult = new RetResult<>();
        retResult.setData(assertTypes);
        retResult.setCode(200);
        retResult.setMsg("查询成功");
        return retResult;
    }

    /**
     * 查询每个断言类型对应的可操作性的断言
     *
     * @param assertTypeName
     * @return
     */
    @ApiOperation(value = "查询每个断言类型对应的可操作性的断言", notes = "查询每个断言类型对应的可操作性的断言")
    @ResponseBody
    @RequestMapping(value = "/getAssertTypeName", method = RequestMethod.POST)
    public RetResult<Object> getAssertTypeName(AssertTypeName assertTypeName) {
        List<AssertTypeName> assertTypeNames = webUIService.getAssertTypeName(assertTypeName);
        RetResult retResult = new RetResult<>();
        retResult.setData(assertTypeNames);
        retResult.setCode(200);
        retResult.setMsg("查询成功");
        return retResult;
    }

    @GetMapping("update")
    public String getUpdateUi() {
        return "webUI/webUiCaseAdd";
    }

    @ApiOperation(value = "获取更新用例", notes = "获取更新用例")
    @PostMapping("getUpdateCase")
    @ResponseBody
    public RetResult<Object> getUpdateCase(WebCase webCase) throws DataException {
        JSONObject res = webUIService.getWebCaseForUpdate(webCase);
        RetResult retResult = new RetResult<>();
        retResult.setData(res);
        retResult.setCode(200);
        retResult.setMsg("查询成功");
        return retResult;
    }

    @ApiOperation(value = "执行一条用例", notes = "执行一条用例")
    @PostMapping("executeCaseSingle")
    @ResponseBody
    public RetResult<Object> executeCase(Integer id) {
        boolean b = executeWebUiCaseService.executeCase(id);
        RetResult retResult = new RetResult<>();
        if (b) {
            retResult.setCode(200);
            retResult.setMsg("正在执行");
        } else {
            retResult.setMsg("执行案例失败，请重新执行");
        }
        return retResult;
    }

    @ApiOperation(value = "删除一条用例", notes = "删除一条用例")
    @PostMapping("deleteWebCase")
    @ResponseBody
    public RetResult<Object> deleteCase(Integer id) {
        boolean b = webUIService.deleteCase(id);
        RetResult retResult = new RetResult<>();
        if (b) {
            retResult.setCode(200);
            retResult.setMsg("删除成功");
        } else {
            retResult.setMsg("删除案例失败，请重新执行");
        }
        return retResult;
    }

    @ApiOperation(value = "执行用例列表", notes = "执行用例列表")
    @PostMapping("executeCaseList")
    @ResponseBody
    public RetResult<Object> executeCaseList(WebCase webCase) {
        List<WebCase> webCases = webUIService.getListWebCase(webCase);
        boolean b = executeWebUiCaseService.executeCaseList(webCases);
        RetResult retResult = new RetResult<>();
        if (b) {
            retResult.setCode(200);
            retResult.setMsg("正在执行");
        } else {
            retResult.setMsg("执行案例失败，请重新执行");
        }
        return retResult;
    }

    @ApiOperation(value = "添加平台", notes = "添加平台")
    @PostMapping("/addPlatform")
    @ResponseBody
    public RetResult<Object> addPlatform(WebPlatform webPlatform) {
        boolean b = webUIService.addPlatform(webPlatform);
        RetResult retResult = new RetResult<>();
        if (b) {
            retResult.setCode(200);
            if (webPlatform.getId() != null) {
                retResult.setMsg("测试平台修改成功");
            } else {
                retResult.setMsg("测试平台添加成功");
            }
        } else {
            retResult.setCode(400);
            retResult.setMsg("测试平台添加失败");
        }
        return retResult;
    }

    @ApiOperation(value = "删除平台", notes = "删除平台")
    @PostMapping("/delPlatform")
    @ResponseBody
    public RetResult<Object> delPlatform(WebPlatform webPlatform) {
        boolean b = webUIService.updatePlatform(webPlatform);
        RetResult retResult = new RetResult<>();
        if (b) {
            retResult.setCode(200);
            retResult.setMsg("测试平台删除成功");
        } else {
            retResult.setCode(400);
            retResult.setMsg("测试平台删除失败");
        }
        return retResult;
    }

    @ApiOperation(value = "getWebPlatformByPage", notes = "getWebPlatformByPage")
    @GetMapping("/getWebPlatformByPage")
    public String getWebPlatformByPage(WebPlatform webPlatform, Page page, Model model) {
        if (webPlatform.getIsDelete() == null) {
            webPlatform.setIsDelete('0');
        }

        List<WebPlatform> webPlatforms = webUIService.getPlatformList(webPlatform);
        PageInfo<WebPlatform> pageInfo = new PageInfo<>(webPlatforms);
        model.addAttribute("webPlatformName",webPlatform.getPlatformName());
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("webPlatforms", webPlatforms);
        return "webUI/WebUiPlatformList";
    }
}
