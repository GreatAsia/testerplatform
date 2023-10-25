package com.okay.testcenter.controller.env;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.okay.testcenter.domain.AlarmHistory;
import com.okay.testcenter.domain.AlarmPeople;
import com.okay.testcenter.domain.RetResponse;
import com.okay.testcenter.domain.RetResult;
import com.okay.testcenter.domain.report.MiddleRequestHistory;
import com.okay.testcenter.service.alarm.AlarmHistoryService;
import com.okay.testcenter.service.alarm.AlarmPeopleService;
import com.okay.testcenter.service.middle.MiddleRequestHisoryService;
import com.okay.testcenter.service.middle.MiddleTestHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.okay.testcenter.tools.ExcelUtil.setStyle;
import static com.okay.testcenter.tools.ExceptionUtil.getMessage;


@Api(description = "环境管理接口")
@Controller
@RequestMapping(value = "/monitor")
public class MonitorController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    AlarmHistoryService alarmHistoryService;
    @Resource
    AlarmPeopleService alarmPeopleService;
    @Autowired
    MiddleTestHistoryService middleTestHistoryService;
    @Autowired
    MiddleRequestHisoryService middleRequestHisoryService;


    @GetMapping(value = "/manager")
    public String manager() {

        return "monitor/MonitorManager";
    }

    @GetMapping(value = "/config")
    public String config() {

        return "monitor/MonitorConfig";
    }

    @GetMapping(value = "/env")
    public String env() {

        return "monitor/MonitorEnv";
    }

    @GetMapping(value = "/online")
    public String online(Model model) {


        PageInfo alarmHistoryList = alarmHistoryService.findList(1, 10);

        model.addAttribute("alarmHistoryList", alarmHistoryList);


        return "monitor/MonitorOnline";
    }

    @ApiOperation(value = "获取报警历史的分页数据", notes = "获取报警历史的分页数据")
    @GetMapping(value = "/online/getlist")
    @ResponseBody
    public String getlist(Integer currentPage, Integer pageSize) {

        PageInfo<AlarmHistory> alarmHistoryPageInfo = alarmHistoryService.findList(currentPage, pageSize);
        return JSONObject.toJSONString(alarmHistoryPageInfo);
    }

    @ApiOperation(value = "按模块导出报警历史", notes = "按模块导出报警历史")
    @GetMapping(value = "/online/export")
    public void exportData(@Validated Integer day, HttpServletResponse response) throws IOException, ParseException {

        SXSSFWorkbook workbook = new SXSSFWorkbook ();
        SXSSFSheet sheet = workbook.createSheet("线上报警数据");
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        //设置自动换行
        style.setWrapText(true);
        // 垂直靠左水平居中
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style = setStyle(style, font);

        String fileName = "alarmData.xlsx";

        //新增数据行，并且设置单元格数据
        String[] headers = {"ID", "任务ID", "服务名称","结果","requestId", "错误类型", "RD", "QA", "时间", "平台链接"};
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
        //自动调整列宽
        sheet.trackAllColumnsForAutoSizing();

        // 解决自动设置列宽中文失效的问题
        sheet.trackAllColumnsForAutoSizing();
        sheet.autoSizeColumn(0, true);
        sheet.setColumnWidth(0, sheet.getColumnWidth(0) * 13 / 10);

        SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        Long useDay = day * 7 * 24 * 60 * 60 * 1000L;
        int count = 1;
        Date nowTime = new Date();
        List<AlarmHistory> alarmHistoryList = alarmHistoryService.list();
        List<AlarmPeople> alarmPeopleList = alarmPeopleService.list();

        for (AlarmHistory a : alarmHistoryList) {

            String qa = "";
            String rd = "";
            Date historyTime = df.parse(a.getTime());
            if (nowTime.getTime() - historyTime.getTime() > useDay) {
                break;
            }
            //只导出钉钉报警的数据
            if(!"true".equals(a.getAlarm())){
                continue;
            }

            for (AlarmPeople people : alarmPeopleList) {
                if (a.getServiceName().equals(people.getProject())) {
                    qa = people.getQa_name();
                    rd = people.getRd_name();
                    break;
                }
            }
            //查询结果和requestId
            String[] content = a.getContent().split("/");
            int history = 0;
            if(content.length > 1){
                history = Integer.parseInt(content[content.length - 1]);
            }
            List<MiddleRequestHistory> middleRequestHistoryList = middleRequestHisoryService.findHistoryByHistoryId(history);
            List<String> requestId = new ArrayList<>();
            for (MiddleRequestHistory m : middleRequestHistoryList){
                if("PASS".equals(m.getResult())){
                    break;
                }
                requestId.add(m.getRequestId());
            }

            SXSSFRow row1 = sheet.createRow(count);

            for (int index = 0; index < 10; ++index) {
                Cell cell = row1.createCell(index);
                cell.setCellStyle(style);

                switch (index) {
                    case 0:
                        cell.setCellValue(a.getId());
                        break;
                    case 1:
                        cell.setCellValue(a.getTaskId());
                        break;
                    case 2:
                        cell.setCellValue(a.getServiceName());
                        break;
                    case 3:
                        cell.setCellValue("FAIL");
                        break;
                    case 4:
                        cell.setCellValue(requestId.toString());
                        break;
                    case 5:
                        cell.setCellValue(a.getErrorType());
                        break;
                    case 6:
                        cell.setCellValue(rd);
                        break;
                    case 7:
                        cell.setCellValue(qa);
                        break;
                    case 8:
                        cell.setCellValue(a.getTime());
                        break;
                    case 9:
                        cell.setCellValue(a.getContent());
                        break;
                    default:
                        logger.error("错误的列号=={}", index);
                        break;

                }
                sheet.trackAllColumnsForAutoSizing();
                sheet.autoSizeColumn(index, true);
                int width = sheet.getColumnWidth(index) * 13 / 10;
                if (width < 255 * 256) {
                    sheet.setColumnWidth(index, width);
                } else {
                    sheet.setColumnWidth(index, 20000);
                }
            }
            count++;

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


    @ApiOperation(value = "获取服务对应的手机号", notes = "获取服务对应的手机号")
    @GetMapping(value = "/service")
    public String getServiceList(Model model) {

        List<AlarmPeople> alarmPeopleList = alarmPeopleService.list();
        model.addAttribute("alarmPeopleList", alarmPeopleList);
        return "monitor/MonitorService";
    }

    @ApiOperation(value = "添加服务对应的手机号", notes = "添加服务对应的手机号")
    @PostMapping(value = "/service/insert")
    @ResponseBody
    public RetResult<Object> insertService(@RequestBody AlarmPeople alarmPeople) {
        alarmPeople.setCreate_time(new Date());
        alarmPeople.setUpdate_time(new Date());
        alarmPeopleService.insert(alarmPeople);
        return RetResponse.makeOKRsp();

    }

    @ApiOperation(value = "更新服务对应的手机号", notes = "更新服务对应的手机号")
    @PostMapping(value = "/service/update")
    @ResponseBody
    public RetResult<Object> updateService(@RequestBody AlarmPeople alarmPeople) {
        alarmPeople.setUpdate_time(new Date());
        alarmPeopleService.update(alarmPeople);
        return RetResponse.makeOKRsp();

    }

    @ApiOperation(value = "删除服务对应的手机号", notes = "删除服务对应的手机号")
    @PostMapping(value = "/service/delete")
    @ResponseBody
    public RetResult<Object> deleteService(@Validated @RequestBody JSONObject request) {
        int id = Integer.parseInt(request.get("id").toString());
        alarmPeopleService.delete(id);
        return RetResponse.makeOKRsp();
    }


}
