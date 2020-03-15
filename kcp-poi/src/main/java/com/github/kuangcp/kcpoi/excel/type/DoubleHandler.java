package com.github.kuangcp.kcpoi.excel.type;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 * Created by https://github.com/kuangcp on 18-3-12  下午7:24
 *
 * @author kuangcp
 */
public class DoubleHandler implements LoadCellValue {

  @Override
  public Cell loadValue(Row row, int index, Object value) {
    Cell cell = row.createCell(index, CellType.NUMERIC);
    cell.setCellValue(Double.parseDouble(value.toString()));
    return cell;
  }
}
