package com.github.kuangcp.kcpoi.excel;

import com.github.kuangcp.kcpoi.config.ConfigManager;
import com.github.kuangcp.kcpoi.config.MainConfig;
import com.github.kuangcp.kcpoi.excel.base.ExcelTransform;
import com.github.kuangcp.kcpoi.excel.type.BooleanHandler;
import com.github.kuangcp.kcpoi.excel.type.DateHandler;
import com.github.kuangcp.kcpoi.excel.type.DoubleHandler;
import com.github.kuangcp.kcpoi.excel.type.FloatHandler;
import com.github.kuangcp.kcpoi.excel.type.IntegerHandler;
import com.github.kuangcp.kcpoi.excel.type.LoadCellValue;
import com.github.kuangcp.kcpoi.excel.type.LongHandler;
import com.github.kuangcp.kcpoi.excel.type.StringHandler;
import com.github.kuangcp.kcpoi.excel.util.ReadAnnotationUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Created by https://github.com/kuangcp on 18-2-21  下午12:47 Excel导出工具类 TODO 异常的合理处理
 * 目前单元格的类型尚不支持公式和错误 Boolean 缺省为false, 字符串缺省为空串, 数值类型为空则是0
 *
 * @author kuangcp
 */
@Slf4j
public final class ExcelExport {

  private static final MainConfig mainConfig = ConfigManager.getInstance();
  private static final Map<String, LoadCellValue> handlerMap = new HashMap<>(16);

  // 字典结合策略模式简化代码
  static {
    handlerMap.put("String", new StringHandler());
    handlerMap.put("Date", new DateHandler());
    handlerMap.put("Boolean", new BooleanHandler());
    handlerMap.put("Long", new LongHandler());
    handlerMap.put("Integer", new IntegerHandler());
    handlerMap.put("Double", new DoubleHandler());
    handlerMap.put("Float", new FloatHandler());

    handlerMap.put("int", new IntegerHandler());
    handlerMap.put("boolean", new BooleanHandler());
    handlerMap.put("long", new LongHandler());
    handlerMap.put("double", new DoubleHandler());
    handlerMap.put("float", new FloatHandler());
  }

  private ExcelExport() {

  }

  /**
   * @param path 文件的绝对路径
   * @param data 主要数据
   */
  public static <T extends ExcelTransform> boolean exportExcel(Workbook workbook,
      String path, List<T> data) {

    if (!ConfigManager.checkConfig(mainConfig)) {
      return false;
    }
    if (Objects.isNull(data) || data.isEmpty()) {
      log.warn("export data is empty");
      return false;
    }
    try {
      File file = new File(path);
      OutputStream out = new FileOutputStream(file);
      return exportExcel(workbook, out, data);
    } catch (FileNotFoundException e) {
      log.error("file not found", e);
      return false;
    }
  }

  /**
   * @param outputStream 输出流
   * @param data 原始对象集合 不为空
   */
  public static <T extends ExcelTransform> boolean exportExcel(Workbook workbook,
      OutputStream outputStream, List<T> data) {

    if (!ConfigManager.checkConfig(mainConfig)) {
      return false;
    }
    try {
      Class<? extends ExcelTransform> target = data.get(0).getClass();
      String sheetTitle = ReadAnnotationUtil.getSheetExportTitle(target);
      List<Object[]> dataList = ReadAnnotationUtil.getContentByList(target, data);

      if (Objects.isNull(dataList)) {
        log.error("{} 中没有已注解的字段, 导出失败", target.getSimpleName());
        return false;
      }

      Sheet sheet = workbook.createSheet(sheetTitle);
      CellStyle columnTitleStyle = getColumnTitleCellStyle(workbook);
      CellStyle style = getContentCellStyle(workbook);

      setSheetTitle(sheet, dataList, sheetTitle, columnTitleStyle);
      setColumnTitle(dataList, sheet, target, columnTitleStyle);
      setContent(style, dataList, sheet);
      workbook.write(outputStream);
    } catch (Exception e) {
      log.error("export error ", e);
      return false;
    }
    return true;
  }

  /**
   * 设置sheet的列头
   */
  private static void setColumnTitle(List<Object[]> dataList, Sheet sheet,
      Class<? extends ExcelTransform> target, CellStyle columnTopStyle) {
    List<ExcelCellMeta> metaList = ReadAnnotationUtil.getCellMetaData(target);
    Row row = sheet.createRow(mainConfig.getTitleLastRowNum());
    int columnNum = dataList.get(0).length;
    for (int n = 0; n < columnNum; n++) {
      //创建列头对应个数的单元格
      Cell cellRowName = row.createCell(n);
      //设置列头单元格的数据类型

      cellRowName.setCellType(CellType.STRING);
      cellRowName.setCellValue(metaList.get(n).getTitle());
      cellRowName.setCellStyle(columnTopStyle);
    }
  }

  /**
   * 根据List来创造出一行的cell, 使用策略模式是因为要从多种的对象类型转换成Excel的特定类型
   */
  private static void createRowCell(CellStyle style, Object[] obj, int index, Row row) {
    Object temp = obj[index];
    Cell cell = handlerMap.get(temp.getClass().getSimpleName()).loadValue(row, index, temp);

    if (Objects.nonNull(cell)) {
      cell.setCellStyle(style);
    }
  }

  /**
   * cell分为: 空格 布尔类型(TRUE FALSE) 字符串 数值 | 错误 公式 填充sheet内容
   */
  private static void setContent(CellStyle style, List<Object[]> dataList, Sheet sheet) {
    for (int m = 0; m < dataList.size(); m++) {
      Object[] obj = dataList.get(m);
      Row row = sheet.createRow(m + mainConfig.getContentStartNum());
      for (int j = 0; j < obj.length; j++) {
        createRowCell(style, obj, j, row);
      }
    }
  }

  /**
   * 设置表格标题行 合并单元格 并 居中
   */
  private static void setSheetTitle(Sheet sheet, List<Object[]> dataList, String sheetTitle,
      CellStyle columnTitleStyle) {

    if (mainConfig.getTitleLastRowNum() < 1) {
      return;
    }
    Row row = sheet.createRow(mainConfig.getStartRowNum());
    Cell cellTitle = row.createCell(mainConfig.getStartColNum());

    int lastColNum = dataList.get(0).length - 1;
    log.debug("title cell range : firstRow={} lastRow={} firstCol={} lastCol={}",
        mainConfig.getStartRowNum(), mainConfig.getTitleLastRowNum() - 1,
        mainConfig.getStartColNum(), lastColNum);

    sheet.addMergedRegion(new CellRangeAddress(mainConfig.getStartRowNum(),
        mainConfig.getTitleLastRowNum() - 1, mainConfig.getStartColNum(), lastColNum));

    log.debug("title value position: cell ={}", cellTitle.getAddress().toString());
    cellTitle.setCellStyle(columnTitleStyle);
    cellTitle.setCellValue(sheetTitle);
  }

  /**
   * 列头单元格样式
   */
  private static CellStyle getColumnTitleCellStyle(Workbook workbook) {
    Font font = workbook.createFont();
    font.setFontHeightInPoints(mainConfig.getTitleFontSize());
    font.setBold(mainConfig.isTitleFontBold());
    font.setFontName(mainConfig.getTitleFontName());

    CellStyle style = workbook.createCellStyle();
    style.setBorderBottom(BorderStyle.MEDIUM);
    style.setBorderRight(BorderStyle.MEDIUM);
    style.setBorderTop(BorderStyle.MEDIUM);

    style.setFont(font);
    //设置自动换行;
    style.setWrapText(false);
    //设置水平对齐的样式为居中对齐;
    style.setAlignment(HorizontalAlignment.CENTER);
    //设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    return style;
  }

  /**
   * 列数据信息单元格样式
   */
  private static CellStyle getContentCellStyle(Workbook workbook) {
    // 设置字体
    Font font = workbook.createFont();
    font.setFontHeightInPoints(mainConfig.getContentFontSize());
    font.setFontName(mainConfig.getContentFontName());
    font.setBold(mainConfig.isContentFontBold());

    CellStyle style = workbook.createCellStyle();
    // 设置边框风格和颜色
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);

    style.setFont(font);
    //设置自动换行;
    style.setWrapText(false);
    //设置水平对齐的样式为居中对齐;
    style.setAlignment(HorizontalAlignment.CENTER);
    //设置垂直对齐的样式为居中对齐;
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    return style;
  }
}