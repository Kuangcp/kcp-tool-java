package com.github.kuangcp.kcpoi.excel.type;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Created by https://github.com/kuangcp on 18-3-12  上午11:14
 *
 * @author kuangcp
 */
public interface LoadCellValue {

  /**
   * 创建row的cell, 根据传入的String值
   *
   * @param row HSSFRow
   * @param index cell的下标0开始
   * @param value 装载的值
   * @return HSSFCell 单元格对象
   */
  Cell loadValue(Row row, int index, Object value);
}
