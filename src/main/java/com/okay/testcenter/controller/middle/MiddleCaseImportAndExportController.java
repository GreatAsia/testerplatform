package com.okay.testcenter.controller.middle;

import com.alibaba.fastjson.JSONObject;
import com.okay.testcenter.domain.middle.MiddleCase;
import com.okay.testcenter.domain.middle.MiddleInterface;
import com.okay.testcenter.service.middle.MiddleCaseService;
import com.okay.testcenter.service.middle.MiddleInterfaceService;
import com.okay.testcenter.tools.ExceptionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.okay.testcenter.tools.ExcelUtil.readExcelFile;
import static com.okay.testcenter.tools.ExcelUtil.setStyle;
import static com.okay.testcenter.tools.ExceptionUtil.getMessage;

/**
 * @author zhou
 * @date 2020/10/16
 */
@Api(description = "中间层用例接口")
@Controller
@RequestMapping(value = "/middle")
public class MiddleCaseImportAndExportController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MiddleInterfaceService middleInterfaceService;
    @Autowired
    MiddleCaseService middleCaseService;

    @ApiOperation(value = "按模块导出中间层用例", notes = "按模块导出中间层用例")
    @GetMapping(value = "/case/module/export")
    public void exportModule(@Validated String projectName, Integer envId, Integer moduleId, HttpServletResponse response) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(projectName);
        CellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();

        //设置自动换行
        style.setWrapText(true);
        // 垂直靠左水平居中
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style = setStyle(style, font);

        String fileName = projectName + ".xls";
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"服务名", "接口名称", "测试内容", "测试结果", "URL", "备注", "请求参数", "校验数据", "用例ID", "环境ID", "接口ID", "用例类型", "附属自动化ID", "是否自动生成"};
        //headers表示excel表中第一行的表头
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle = setStyle(headerStyle, font);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerStyle.setAlignment(HorizontalAlignment.CENTER);


        HSSFRow row = sheet.createRow(0);
        row.setHeightInPoints(20);
        //在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            cell.setCellStyle(headerStyle);

        }
        sheet.autoSizeColumn(0, true);
        // 解决自动设置列宽中文失效的问题
        sheet.setColumnWidth(0, sheet.getColumnWidth(0) * 17 / 10);

        List<MiddleInterface> middleInterfaces = middleInterfaceService.findMiddleInterfaceByModuleId(moduleId);

        int count = 1;
        for (MiddleInterface i : middleInterfaces) {

            List<MiddleCase> middleCaseList = middleCaseService.findMiddleCaseByEnvAndInterface(envId, i.getId());
            //在表中存放查询到的数据放入对应的列
            count = rowNum;
            for (MiddleCase c : middleCaseList) {
                HSSFRow row1 = sheet.createRow(rowNum);

                for (int index = 0; index < 14; ++index) {
                    HSSFCell cell = row1.createCell(index);
                    cell.setCellStyle(style);
                    switch (index) {
                        case 0:
                            if (rowNum == 1) {
                                cell.setCellValue(projectName);
                            } else {
                                cell.setCellValue("");
                            }
                            break;
                        case 1:
                            if (count == rowNum) {
                                cell.setCellValue(i.getName());
                            } else {
                                cell.setCellValue("");
                            }
                            break;
                        case 2:
                            cell.setCellValue(c.getName());
                            break;
                        case 3:
                            cell.setCellValue("");
                            break;
                        case 4:
                            if (count == rowNum) {
                                cell.setCellValue(i.getUrl());
                            } else {
                                cell.setCellValue("");
                            }
                            break;
                        case 5:
                            cell.setCellValue("");
                            break;
                        case 6:
                            cell.setCellValue(c.getRequest_data());
                            break;
                        case 7:
                            cell.setCellValue(c.getCheck_data());
                            break;
                        case 8:
                            cell.setCellValue(c.getId());
                            break;
                        case 9:
                            cell.setCellValue(c.getEnv_id());
                            break;
                        case 10:
                            cell.setCellValue(c.getInterface_id());
                            break;
                        case 11:
                            cell.setCellValue(c.getCaseType());
                            break;
                        case 12:
                            cell.setCellValue(c.getRef_id());
                            break;
                        case 13:
                            cell.setCellValue(c.getAuto());
                            break;
                        default:
                            break;

                    }
                    sheet.autoSizeColumn(index, true);
                    int width = sheet.getColumnWidth(index) * 17 / 10;
                    if(width < 255*256){
                        sheet.setColumnWidth(index, width);
                    }else {
                        sheet.setColumnWidth(index, 20000);
                    }

                }

                rowNum++;
            }
            if (middleCaseList.size() > 1) {
                CellRangeAddress name = new CellRangeAddress(count, rowNum - 1, 1, 1);
                CellRangeAddress url = new CellRangeAddress(count, rowNum - 1, 4, 4);
                sheet.addMergedRegion(name);
                sheet.addMergedRegion(url);
            }

        }
        if (rowNum > 2) {
            CellRangeAddress serviceName = new CellRangeAddress(1, rowNum - 1, 0, 0);
            sheet.addMergedRegion(serviceName);
        }


        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());

    }




    @ApiOperation(value = "按模块导入中间层用例", notes = "按模块导入中间层用例")
    @PostMapping(value = "/case/module/import")
    public String uploadExcel(MultipartFile file, Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("importInfo", "文件为空,请上传.xls文件");
            return "middle/MiddleCaseImport";
        }
        String name = file.getOriginalFilename();
        logger.info("文件名 {}", name);
        if (name.length() < 6 || !name.substring(name.length() - 4).equals(".xls")) {
            model.addAttribute("importInfo", "文件格式错误,请上传.xls文件");
            return "middle/MiddleCaseImport";
        }

        try {
            List<MiddleCase> middleCaseList = readExcelFile(file.getInputStream());
            logger.info("caseList {}", JSONObject.toJSONString(middleCaseList));
            for (MiddleCase m : middleCaseList) {
                if (m.getId() == 0) {
                    middleCaseService.insertMiddleCase(m);
                } else {
                    middleCaseService.updateMiddleCase(m);
                }
            }
            model.addAttribute("importInfo", "上传成功!");
        } catch (Exception e) {
            logger.error("上传失败=={}",getMessage(e));
            e.printStackTrace();

            model.addAttribute("importInfo", "上传失败:" + ExceptionUtil.getMessage(e));
        }

        return "middle/MiddleCaseImport";

    }




}
