package com.okay.testcenter.controller.ui;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.Page;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.report.UiPadCaseList;
import com.okay.testcenter.domain.report.UiPadRunIdList;
import com.okay.testcenter.domain.report.UiPadSerialnoList;
import com.okay.testcenter.service.ui.UiPadCaseListService;
import com.okay.testcenter.service.ui.UiPadRunIdListService;
import com.okay.testcenter.service.ui.UiPadSerialnoListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Api(description = "Pad自动化报告接口")
@Controller
@RequestMapping(value = "/uiPad/report")

public class UiPadReportController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${web.upload-path}")
    private String path;

    @Resource
    UiPadRunIdListService uiPadRunIdListService;
    @Resource
    UiPadSerialnoListService uiPadSerialnoListService;
    @Resource
    UiPadCaseListService uiPadCaseListService;

    @GetMapping(value = "/list")
    public String listUiReport(Model model) {

        PageInfo pageInfo = uiPadRunIdListService.findUiPadRunList(1, 10);
        logger.info("[pageInfo]:" + JSONObject.toJSON(pageInfo));
        model.addAttribute("pageInfo", pageInfo);
        return "report/UiPadRunIdList";
    }

    @ApiOperation(value = "获取Pad报告列表", notes = "获取Pad报告列表")
    @GetMapping(value = "/getlist")
    @ResponseBody
    public String searchUiPadRunIdList(@Validated Page page) {

        PageInfo pageInfo = uiPadRunIdListService.findUiPadRunList(page.getCurrentPage(), page.getPageSize());
        return JSONObject.toJSONString(pageInfo);
    }

    @ApiOperation(value = "添加Pad报告", notes = "添加Pad报告")
    @PostMapping(value = "/insert")
    @ResponseBody
    public RetResult<Object> insertUiPadRunIdList(@Validated @RequestBody UiPadRunIdList uiPadRunIdList) {

        uiPadRunIdListService.insertUiPadRunIdList(uiPadRunIdList);
        return RetResponse.makeOKRsp();
    }

    @ApiOperation(value = "更新Pad报告", notes = "更新Pad报告")
    @PostMapping(value = "/update")
    @ResponseBody
    public RetResult<Object> updateUiPadRunIdList(@Validated @RequestBody UiPadRunIdList uiPadRunIdList) {

        uiPadRunIdListService.updateUiPadRunIdList(uiPadRunIdList);
        return RetResponse.makeOKRsp();
    }

    @ApiOperation(value = "获取Pad报告最新的运行ID", notes = "获取Pad报告最新的运行ID")
    @GetMapping(value = "/lastRunId")
    @ResponseBody
    public int getLastRunId() {
        //先生成一个新的runId，然后后面更新
        UiPadRunIdList uiPadRunIdList = new UiPadRunIdList();
        uiPadRunIdList.setTotalDevice(1);
        uiPadRunIdList.setPassDevice(1);
        uiPadRunIdList.setFailDevice(1);
        uiPadRunIdList.setErrorDevice(1);
        uiPadRunIdList.setElapsedTime("");
        uiPadRunIdList.setStartTime("");
        uiPadRunIdList.setPassRate("");

        uiPadRunIdListService.insertUiPadRunIdList(uiPadRunIdList);
        return uiPadRunIdListService.getLastId();
    }

    @ApiOperation(value = "删除Pad报告", notes = "删除Pad报告")
    @PostMapping(value = "/delete")
    @ResponseBody
    public RetResult<Object> deleteUiPadRunIdList(@Validated @RequestBody JSONObject request) {
        int id = Integer.parseInt(request.get("id").toString());
        uiPadRunIdListService.deleteUiPadById(id);
        return RetResponse.makeOKRsp();
    }


    @GetMapping(value = "/serialnolist/{id}")
    public String uiPadSerialnoList(@PathVariable("id") Integer id, Model model) {

        List<UiPadSerialnoList> uiPadSerialnoLists = uiPadSerialnoListService.findUiPadSerialnoListByRunId(id);
        model.addAttribute("uiPadSerialnoLists", uiPadSerialnoLists);
        return "report/UiPadSerialnoList";

    }

    @ApiOperation(value = "添加Pad报告序列号", notes = "添加Pad报告序列号")
    @PostMapping(value = "/serialnolist/insert")
    @ResponseBody
    public RetResult<Object> insertUiPadSerialnoList(@Validated @RequestBody UiPadSerialnoList uiPadSerialnoList) {

        uiPadSerialnoListService.insertUiPadSerialnoList(uiPadSerialnoList);
        logger.info("[responseInfo]==" + RetResponse.makeOKRsp());
        return RetResponse.makeOKRsp();
    }


    @GetMapping(value = "/caseList")
    public String uiPadCaseList(int runId, String serialno, Model model) {
        List<String> path = new ArrayList<>();
        List<UiPadCaseList> uiPadCaseLists = uiPadCaseListService.findUiPadCaseListByRunIdAndSerialno(runId,serialno);
        for(UiPadCaseList uiPadCaseList : uiPadCaseLists){
            if (uiPadCaseList.getPicturePath() == null || "".equals(uiPadCaseList.getPicturePath())) {
                continue;
            }else {
                String[] pathlist = uiPadCaseList.getPicturePath().split(";");
                for (int s = 0; s < pathlist.length; s++) {
                    path.add(pathlist[s]);
                }
                uiPadCaseList.setPicturePathList(path);
            }
        }
        model.addAttribute("uiPadCaseLists", uiPadCaseLists);
        UiPadSerialnoList uiPadSerialnoLists = uiPadSerialnoListService.findUiPadSerialnoListByRunIdAndSerialno(runId, serialno);
        model.addAttribute("uiPadSerialnoLists", uiPadSerialnoLists);
        return "report/UiPadCaseList";
    }

    @ApiOperation(value = "添加Pad报告用例", notes = "添加Pad报告用例")
    @PostMapping(value = "/caseList/insert")
    @ResponseBody
    public RetResult<Object> insertUiPadCaseList(@Validated @RequestBody UiPadCaseList uiPadCaseList) {

        uiPadCaseListService.insertUiPadCaseList(uiPadCaseList);
        logger.info("[responseInfo]==" + RetResponse.makeOKRsp());
        return RetResponse.makeOKRsp();
    }

    @ApiOperation(value = "Pad报告添加文件", notes = "Pad报告添加文件")
    @PostMapping(value = "/insertFile")
    @ResponseBody
    public RetResult<Object> insertFile(@Validated String filePath, MultipartFile saveFile ) {

        File file = new File(path + filePath);
        logger.info("[filePath]" + file.getPath() );
        if(!file.getParentFile().exists()){
            boolean result = file.getParentFile().mkdirs();
            if (true == result) {
                logger.info("创建文件路径成功==" + file.getPath());
            }else {
                logger.info("创建文件路径失败=" + file.getPath());
            }
        }
        try {
            saveFile.transferTo(file);

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            logger.info("保存文件失败!!! "+ e.getLocalizedMessage());
            RetResponse.makeErrRsp("上传文件失败!!!");
        }

        return RetResponse.makeOKRsp();
    }


    @GetMapping(value = "/info/{id}")
    public String uiReport(@PathVariable("id") Integer id, Model model) {

        UiPadRunIdList uiPadRunIdList = uiPadRunIdListService.findUiPadRunIdList(id);
        model.addAttribute("uiPadRunIdList", uiPadRunIdList);
        List<UiPadCaseList> uiPadCaseLists = uiPadCaseListService.findUiPadCaseListByRunId(id);
        UiPadSerialnoList caseResult = new UiPadSerialnoList();
        int pass = 0;
        int fail = 0;
        int x = 0;
        for (UiPadCaseList uiPadCaseList1 : uiPadCaseLists) {
            uiPadCaseLists.get(x).setId(x + 1);
            if ("true".equals(uiPadCaseList1.getCaseResult())) {
                pass++;
            } else {
                fail++;
            }
            x++;
        }
        //@TODO setCountCase需要修改
        caseResult.setCountCase(35);
        caseResult.setPassCase(pass);
        caseResult.setFailCase(fail);
        caseResult.setTotalCase(pass + fail);
        caseResult.setPassRate(String.format("%.2f", (((float) pass / (pass + fail)) * 100)) + "%");
        model.addAttribute("caseResult", caseResult);
        model.addAttribute("uiPadCaseLists", uiPadCaseLists);

        StringBuilder romVersionBuilder = new StringBuilder();
        StringBuilder apkVersionBuilder = new StringBuilder();
        StringBuilder envBuilder = new StringBuilder();
        StringBuilder netWorkBuilder = new StringBuilder();
        StringBuilder versionBuilder = new StringBuilder();
        List<UiPadSerialnoList> uiPadSerialnoLists = uiPadSerialnoListService.findUiPadSerialnoListByRunId(id);
        UiPadSerialnoList uiPadSerialno = new UiPadSerialnoList();
        int i = 0;
        for (UiPadSerialnoList uiPadSerialnoList : uiPadSerialnoLists) {
            if (i == 0) {
                romVersionBuilder.append(uiPadSerialnoList.getRomVersion());
                apkVersionBuilder.append(uiPadSerialnoList.getApkVersion());
                envBuilder.append(uiPadSerialnoList.getEnv());
                netWorkBuilder.append(uiPadSerialnoList.getNetWork());
                versionBuilder.append(uiPadSerialnoList.getVersion());
            }
            if (romVersionBuilder.indexOf(uiPadSerialnoList.getRomVersion()) == -1) {
                romVersionBuilder.append("/" + uiPadSerialnoList.getRomVersion());
            }
            if (apkVersionBuilder.indexOf(uiPadSerialnoList.getApkVersion()) == -1) {
                apkVersionBuilder.append("/").append(uiPadSerialnoList.getApkVersion());
            }
            if (envBuilder.indexOf(uiPadSerialnoList.getEnv()) == -1) {
                envBuilder.append("/").append(uiPadSerialnoList.getEnv());
            }
            if (netWorkBuilder.indexOf(uiPadSerialnoList.getNetWork()) == -1) {
                netWorkBuilder.append("/").append(uiPadSerialnoList.getNetWork());
            }
            if (versionBuilder.indexOf(uiPadSerialnoList.getVersion()) == -1) {
                versionBuilder.append("/").append(uiPadSerialnoList.getVersion());
            }
            i++;
        }
        uiPadSerialno.setRomVersion(romVersionBuilder.toString());
        uiPadSerialno.setApkVersion(apkVersionBuilder.toString());
        uiPadSerialno.setEnv(envBuilder.toString());
        uiPadSerialno.setNetWork(netWorkBuilder.toString());
        uiPadSerialno.setVersion(versionBuilder.toString());
        model.addAttribute("uiPadSerialno", uiPadSerialno);

        return "report/UiPadReport";

    }


}
