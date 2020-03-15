package com.github.kuangcp.kcpoi.excel.type;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 * Created by https://github.com/kuangcp on 18-3-12  下午7:33
 *
 * @author kuangcp
 */
public class LongHandler implements LoadCellValue {

  @Override
  public Cell loadValue(Row row, int index, Object value) {
    Cell cell = row.createCell(index, CellType.NUMERIC);
    cell.setCellValue(Long.parseLong(value.toString()));
    return cell;
  }
}
