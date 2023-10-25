package com.okay.testcenter.tools;

import com.okay.testcenter.domain.middle.MiddleCase;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhou
 * @date 2020/11/16
 */
public class ExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);


    public static CellStyle setStyle(CellStyle style, Font hssfFont) {
        // 设置上下左右四个边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        //设置颜色
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());

        //设置字体
        hssfFont.setFontHeightInPoints((short) 10);
        hssfFont.setFontName("微软雅黑");
        hssfFont.setItalic(false);
        hssfFont.setStrikeout(false);
        style.setFont(hssfFont);

        return style;
    }

    public static List<MiddleCase> readExcelFile(InputStream inputStream) {
        Workbook workbook = null;
        List<MiddleCase> middleCaseList = new ArrayList<>();

        try {
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            //总行数
            int rowLength = sheet.getLastRowNum();
            logger.info("总行数 {}", rowLength);
            //工作表的列
            Row row = sheet.getRow(0);
            //总列数
            int colLength = row.getLastCellNum();
            logger.info("总列数 {}", colLength);
            //得到指定的单元格
            Cell cell = row.getCell(0);

            String data = "";
            for (int i = 1; i <= rowLength; ++i) {
                row = sheet.getRow(i);
                MiddleCase middleCase = new MiddleCase();
                for (int j = 2; j < colLength; ++j) {

                    if (j == 3 || j == 4 || j == 5) {
                        continue;
                    }
                    cell = row.getCell(j);
                    //设置单元格类型
                    cell.setCellType(CellType.STRING);
                    data = cell.getStringCellValue();
                    if (data == null || data.equals("")) {
                        data = "0";
                    }
                    switch (j) {
                        case 2:
                            middleCase.setName(data);
                            break;
                        case 6:
                            middleCase.setRequest_data(data);
                            break;
                        case 7:
                            middleCase.setCheck_data(data);
                            break;
                        case 8:
                            middleCase.setId(Integer.parseInt(data));
                            break;
                        case 9:
                            middleCase.setEnv_id(Integer.parseInt(data));
                            break;
                        case 10:
                            middleCase.setInterface_id(Integer.parseInt(data));
                            break;
                        case 11:
                            middleCase.setCaseType(Integer.parseInt(data));
                            break;
                        case 12:
                            middleCase.setRef_id(Integer.parseInt(data));
                            break;
                        case 13:
                            middleCase.setAuto(Integer.parseInt(data));
                            break;
                        default:
                            logger.error("列号错误 {}", j);
                            break;

                    }

                }
                middleCaseList.add(middleCase);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }


        return middleCaseList;
    }
}
