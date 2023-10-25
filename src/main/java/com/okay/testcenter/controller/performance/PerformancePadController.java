package com.okay.testcenter.controller.performance;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.report.PerformanceHistory;
import com.okay.testcenter.service.performance.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.okay.testcenter.tools.GetTime.getTotalTime;

@Api(description = "性能测试(Pad)接口")
@RequestMapping(value = "/performance/pad")
@Controller
public class PerformancePadController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static String FAIL = "FAIL";
    private final static String ELECTRIC = "电量";


    @Resource
    ReportService reportService;


    @ApiOperation(value = "添加APP端性能数据", notes = "添加APP端性能数据")
    @PostMapping(value = "/insert")
    @ResponseBody
    public PerformanceHistory insertPerformance(@Validated PerformanceHistory performanceHistory) throws ParseException {
        //插入数据
        reportService.insertReport(performanceHistory);

        List<PerformanceHistory> performanceHistoryList = reportService.findReportByRunId(performanceHistory.getRunId());
        SimpleDateFormat df =  new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = df.parse(performanceHistoryList.get(0).getRunTime());
        Date endTime = df.parse(performanceHistoryList.get(performanceHistoryList.size() - 1).getRunTime());
        String totalTime = getTotalTime(endTime, startTime);
        for (PerformanceHistory p : performanceHistoryList) {
            //更新测试结果 TODO:需要优化
            if (FAIL.equals(performanceHistory.getResult()) && ELECTRIC.equals(performanceHistory.getType())) {
                p.setResult("FAIL");
            }
            p.setTotalTime(totalTime);
            reportService.updateReport(p);
        }

        logger.info("insertReport==" + JSONObject.toJSONString(performanceHistory));
        return performanceHistory;
    }

    @ApiOperation(value = "添加APP端性能数据", notes = "添加APP端性能数据")
    @PostMapping(value = "/inserts")
    @ResponseBody
    public RetResult<Object> inserts(@RequestBody List<PerformanceHistory> performanceHistorys) throws ParseException {
        //插入数据

        reportService.insertReports(performanceHistorys);

        List<PerformanceHistory> performanceHistoryList = reportService.findReportByRunId(performanceHistorys.get(0).getRunId());
        SimpleDateFormat df =  new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = df.parse(performanceHistoryList.get(0).getRunTime());
        Date endTime = df.parse(performanceHistoryList.get(performanceHistoryList.size() - 1).getRunTime());
        String totalTime = getTotalTime(endTime, startTime);
        for (PerformanceHistory p : performanceHistoryList) {
            //更新测试结果 TODO:需要优化
            if (FAIL.equals(p.getResult()) && ELECTRIC.equals(p.getType())) {
                p.setResult("FAIL");
            }
            p.setTotalTime(totalTime);
            reportService.updateReport(p);
        }

        return RetResponse.makeOKRsp();
    }

    @GetMapping(value = "/runIdList")
    public List<PerformanceHistory> runIdPerformanceList() {

        return reportService.findRunIdList();

    }

    @ApiOperation(value = "获取最新的运行ID", notes = "获取最新的运行ID")
    @GetMapping(value = "/getLastRunId")
    public @ResponseBody
    String getLastRunIdPerformance() {

        return reportService.getLastRunId() + "";

    }

    @GetMapping(value = "/reportDetail/{id}")
    public String reportDetailPerformance(@PathVariable("id") Integer id, Model model) {

        List<PerformanceHistory> performanceHistoryList = reportService.findReportByRunId(id);
        ArrayList data = new ArrayList();
        ArrayList runtime = new ArrayList();

        for (int i = 0; i < performanceHistoryList.size(); i++) {
            PerformanceHistory performanceHistory = performanceHistoryList.get(i);
            data.add(performanceHistory.getPreSize());
            runtime.add(performanceHistory.getRunTime());
        }
        PerformanceHistory reportone = performanceHistoryList.get(0);
        reportone.setPreSizes(data);
        reportone.setRunTimes(runtime);

        String serialno = performanceHistoryList.get(0).getSerialno();
        model.addAttribute("reports", reportone);
        model.addAttribute("serialno", serialno);
        return "performance/ReportDetail";

    }


    @GetMapping(value = "/reportDownload/{id}")
    public void reportDownloadPerformance(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("性能表");

        List<PerformanceHistory> performanceHistoryList = reportService.findReportByRunId(id);
        String fileName = "preformance" + ".xls";
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"运行ID", "名称", "类型", "序列号", "值", "单位", "结果", "运行时间"};
        //headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //在表中存放查询到的数据放入对应的列
        for (PerformanceHistory performanceHistory : performanceHistoryList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(performanceHistory.getRunId());
            row1.createCell(1).setCellValue(performanceHistory.getName());
            row1.createCell(2).setCellValue(performanceHistory.getType());
            row1.createCell(3).setCellValue(performanceHistory.getSerialno());
            row1.createCell(4).setCellValue(performanceHistory.getPreSize());
            row1.createCell(5).setCellValue(performanceHistory.getUnit());
            row1.createCell(6).setCellValue(performanceHistory.getResult());
            row1.createCell(7).setCellValue(performanceHistory.getRunTime());

            rowNum++;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());

    }

    @ApiOperation(value = "APP性能序列号列表", notes = "APP性能序列号列表")
    @GetMapping(value = "/serialno/getlist")
    @ResponseBody
    public String SerialnoListPerformance(int currentPage, int pageSize) {

        PageInfo serialnoList = reportService.findSerialnoList(currentPage, pageSize);

        return JSONObject.toJSONString(serialnoList);

    }

    @GetMapping(value = "/serialno/list")
    public String listPerformance(Model model) {

        PageInfo serialnoList = reportService.findSerialnoList(1, 10);

        model.addAttribute("serialnoList", serialnoList);

        return "performance/SerialnoList";

    }


    @GetMapping(value = "/serialnoInfo/{serialno}")
    public String SerialnoList(@PathVariable("serialno") String serialno, Model model) {


        PageInfo serialnoList = reportService.findReportBySerialno(1, 10, serialno);
        model.addAttribute("serialnotext", serialno);
        model.addAttribute("serialnoInfo", serialnoList);

        return "performance/SerialnoInfo";

    }

    @ApiOperation(value = "APP性能序列号下面的数量列表", notes = "APP性能序列号下面的数量列表")
    @GetMapping(value = "/serialnoInfo/{serialno}/list")
    @ResponseBody
    public String SerialnoInfo(@PathVariable("serialno") String serialno, int currentPage, int pageSize) {


        PageInfo serialnoList = reportService.findReportBySerialno(currentPage, pageSize, serialno);

        return JSONObject.toJSONString(serialnoList);

    }

    @ApiOperation(value = "APP性能删除序列号", notes = "APP性能删除序列号")
    @PostMapping(value = "/serialno/delete")
    @ResponseBody
    public RetResult<Object> deleteSerialno(@Validated @RequestBody JSONObject request) {
        String serialno = request.get("serialno").toString();
        reportService.deleteReportBySerialno(serialno);

        return RetResponse.makeOKRsp();
    }


    @GetMapping(value = "/serialno/search")
    public String searchSerialno(@RequestParam String serialnoName, Model model) {


        PageInfo serialnoList = reportService.findReportBySerialno(1, 10, serialnoName);
        model.addAttribute("serialnotext", serialnoName);
        model.addAttribute("serialnoInfo", serialnoList);

        return "performance/SerialnoInfo";

    }


}
