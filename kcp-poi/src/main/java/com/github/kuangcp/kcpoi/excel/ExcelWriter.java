package com.github.kuangcp.kcpoi.excel;

import com.github.kuangcp.kcpoi.excel.base.ExcelTransform;
import com.github.kuangcp.kcpoi.excel.domain.DataBlock;
import com.github.kuangcp.kcpoi.excel.type.BooleanHandler;
import com.github.kuangcp.kcpoi.excel.type.DateHandler;
import com.github.kuangcp.kcpoi.excel.type.DoubleHandler;
import com.github.kuangcp.kcpoi.excel.type.FloatHandler;
import com.github.kuangcp.kcpoi.excel.type.IntegerHandler;
import com.github.kuangcp.kcpoi.excel.type.LoadCellValue;
import com.github.kuangcp.kcpoi.excel.type.LongHandler;
import com.github.kuangcp.kcpoi.excel.type.StringHandler;
import com.github.kuangcp.kcpoi.excel.util.ReadAnnotationUtil;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author https://github.com/kuangcp on 2019-12-13 19:12
 */
@Slf4j
public final class ExcelWriter {

  private static Map<String, LoadCellValue> handlerMap = new HashMap<>(7);

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

  private ExcelWriter() {

  }

  public static <T extends ExcelTransform> boolean fillData(Sheet sheet, DataBlock<T> block) {
    if (Objects.isNull(sheet) || Objects.isNull(block) || block.isEmpty()) {
      return false;
    }
    List<T> dataList = block.getData();
    int start = block.getStartNum();
    int end = block.getEndNum();

    try {
      Class<? extends ExcelTransform> target = dataList.get(0).getClass();

      List<ExcelCellMeta> cellMetaData = ReadAnnotationUtil.getCellMetaData(target);

      Map<Integer, Row> rowMap = createRow(start, end, sheet);
      for (int i = 0; i < dataList.size(); i++) {
        Row row = rowMap.get(start + i);
        T data = dataList.get(i);
        for (ExcelCellMeta cellMeta : cellMetaData) {
          PropertyDescriptor propertyDescriptor = new PropertyDescriptor(
              cellMeta.getField().getName(), target);
          Method method = propertyDescriptor.getReadMethod();
          Object result = method.invoke(data);
          Class<?> type = cellMeta.getField().getType();
          createCell(row, result, type);
        }
      }
    } catch (Exception e) {
      log.error("export error ", e);
      return false;
    }
    return true;
  }

  private static void createCell(Row row, Object value, Class<?> type) {
    int col = row.getLastCellNum();
    col = (col == -1) ? 0 : col;
    LoadCellValue loadCellValue = handlerMap.get(type.getSimpleName());
    loadCellValue.loadValue(row, col, value);
  }

  public synchronized static Map<Integer, Row> createRow(int start, int end, Sheet sheet) {
    Map<Integer, Row> result = new HashMap<>(end - start + 1);
    for (int i = start; i <= end; i++) {

      Row row = sheet.createRow(i);
      if (Objects.isNull(row)) {
        log.warn("null : i={}", i);
      }
      result.put(i, row);
    }
    return result;
  }
}
