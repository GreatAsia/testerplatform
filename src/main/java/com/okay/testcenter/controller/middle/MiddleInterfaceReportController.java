package com.okay.testcenter.controller.middle;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.Env;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.middle.MiddleCase;
import com.okay.testcenter.domain.middle.MiddleInterface;
import com.okay.testcenter.domain.middle.MiddleProject;
import com.okay.testcenter.domain.report.MiddleRequestHistory;
import com.okay.testcenter.domain.report.MiddleTestHistory;
import com.okay.testcenter.service.middle.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.okay.testcenter.tools.ExcelUtil.setStyle;
import static com.okay.testcenter.tools.ExceptionUtil.getMessage;

@Api(description = "中间层报告接口")
@Controller
@RequestMapping(value = "/middle/report")
public class MiddleInterfaceReportController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MiddleTestHistoryService middleTestHistoryService;
    @Autowired
    MiddleRequestHisoryService middleRequestHisoryService;
    @Autowired
    MiddleProjectService middleProjectService;
    @Autowired
    MiddleInterfaceService middleInterfaceService;
    @Autowired
    MiddleCaseService middleCaseService;
    @Autowired
    EnvService envService;


    @GetMapping(value = "/list")
    public String MiddleHistoryList(Model model) {

        PageInfo historyList = middleTestHistoryService.findMiddleTestHistoryByEnvAndProjectId(1, 1, 1, 10);
        List<MiddleProject> projectList =  middleProjectService.findMiddleProjectList();
        model.addAttribute("historyList",historyList);
        model.addAttribute("projectList",projectList);
        List<Env> envList = envService.findEnvList();
        model.addAttribute("envList",envList);
        return "report/MiddleInterfaceReport";

    }


    @GetMapping(value = "/detail/{id}")
    public String middleReportDetail(@PathVariable("id") int id, Model model) {

        MiddleTestHistory middleTestHistory = middleTestHistoryService.findMiddleTestHistoryById(id);
        List<MiddleRequestHistory> middleRequestHistoryList = middleRequestHisoryService.findHistoryByHistoryId(id);
        model.addAttribute("middleTestHistory",middleTestHistory);
        model.addAttribute("middleRequestHistoryList",middleRequestHistoryList);

        return "report/MiddleInterfaceReportDetail";
    }

    @ApiOperation(value = "中间层报告列表", notes = "中间层报告列表")
    @GetMapping(value = "/getlist")
    @ResponseBody
    public String getMiddleList(int env_id, int project_id, int currentPage, int pageSize) {

        PageInfo historyList = middleTestHistoryService.findMiddleTestHistoryByEnvAndProjectId(env_id, project_id, currentPage, pageSize);

        return JSONObject.toJSONString(historyList);

    }


    @ApiOperation(value = "导出中间层报告", notes = "导出中间层报告")
    @GetMapping(value = "/export")
    public void exportReport(@Validated Integer historyId, HttpServletResponse response) throws IOException {


        List<MiddleRequestHistory> middleRequestHistoryList = middleRequestHisoryService.findHistoryByHistoryOrderByInterfaceId(historyId);
        MiddleTestHistory middleTestHistory = middleTestHistoryService.findMiddleTestHistoryById(historyId);
        MiddleProject middleProject = middleProjectService.findMiddleProjectById(middleTestHistory.getProjectId());

        SXSSFWorkbook workbook = new SXSSFWorkbook ();
        SXSSFSheet sheet = workbook.createSheet(middleProject.getName());
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        //设置自动换行
        style.setWrapText(true);
        // 垂直靠左水平居中
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style = setStyle(style, font);

        String fileName = middleProject.getName() + ".xlsx";
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"服务名", "接口名称", "用例名称", "测试结果", "预期结果", "实际结果", "requestId", "URL", "请求参数", "备注"};
        //headers表示excel表中第一行的表头
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle = setStyle(headerStyle, font);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        SXSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(20);
        //在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            XSSFRichTextString text = new XSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle);
        }


//        sheet.autoSizeColumn(0, true);

        // 解决自动设置列宽中文失效的问题
        sheet.setColumnWidth(0, sheet.getColumnWidth(0) * 13 / 10);
        int count = 1;
        int interfaceId = middleRequestHistoryList.get(0).getInterfaceId();
        Map<Integer, Integer> map = new TreeMap();
        for (int i = 0; i < middleRequestHistoryList.size(); ++i) {

            MiddleRequestHistory m = middleRequestHistoryList.get(i);
            MiddleInterface middleInterface = middleInterfaceService.findMiddleInterfaceById(m.getInterfaceId());

            SXSSFRow row1 = sheet.createRow(rowNum);

            for (int index = 0; index < 10; ++index) {
                Cell cell = row1.createCell(index);
                cell.setCellStyle(style);
                switch (index) {
                    case 0:
                        if (rowNum == 1) {
                            cell.setCellValue(middleProject.getName());
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 1:
                        if (interfaceId != m.getInterfaceId() || i == 0) {
                            cell.setCellValue(middleInterface.getName());
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 2:
                        cell.setCellValue(m.getCaseName());
                        break;
                    case 3:
                        cell.setCellValue(m.getResult());
                        break;
                    case 4:
                        cell.setCellValue(m.getExpectResult());
                        break;
                    case 5:
                        if(m.getResponseContent().length() >=32700){
                            String responseContent = m.getResponseContent();
                            cell.setCellValue(responseContent.substring(0,32700));
                        }else {
                            cell.setCellValue(m.getResponseContent());
                        }

                        break;
                    case 6:
                        cell.setCellValue(m.getRequestId());
                        break;
                    case 7:
                        if (interfaceId != m.getInterfaceId() || i == 0) {
                            cell.setCellValue(m.getUrl());
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case 8:
                        cell.setCellValue(m.getRequestData());
                        break;
                    case 9:
                        cell.setCellValue("");
                        break;
                    default:
                        break;

                }
//                sheet.trackAllColumnsForAutoSizing();
//                sheet.autoSizeColumn(index, true);
                int width = sheet.getColumnWidth(index) * 13 / 10;
                if (width < 255 * 256) {
                    sheet.setColumnWidth(index, width);
                } else {
                    sheet.setColumnWidth(index, 20000);
                }


            }

            if (i == (middleRequestHistoryList.size() - 1)) {

                if (rowNum - count >= 1) {
                    map.put(count, rowNum );
                }

            }

            if (interfaceId != m.getInterfaceId()) {

                if (rowNum - count > 1) {
                    map.put(count, rowNum - 1);
                }
                count = rowNum;
                interfaceId = m.getInterfaceId();
            }

            rowNum++;

        }
        //合并接口列表
        if ((middleRequestHistoryList.size() > 1) && (map.size() > 0)) {

            for (Integer key : map.keySet()) {
                int value = map.get(key);
                CellRangeAddress name = new CellRangeAddress(key, value, 1, 1);
                CellRangeAddress url = new CellRangeAddress(key, value, 7, 7);
                sheet.addMergedRegion(name);
                sheet.addMergedRegion(url);
            }

        }
        //合并服务列表
        if (rowNum > 2) {
            CellRangeAddress serviceName = new CellRangeAddress(1, rowNum - 1, 0, 0);
            sheet.addMergedRegion(serviceName);
        }

        //创建一个输出流
        try {
            //设置响应信息
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            //一定要设置成xlsx格式
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName , "UTF-8"));
            response.flushBuffer();
            workbook.write(response.getOutputStream());

        }catch (IOException e){
            logger.error("导出excel报错-{}",getMessage(e));
            e.printStackTrace();
        }finally {
            if (workbook != null){
                workbook.close();
            }

        }


    }


}
