package com.github.kuangcp.kcpoi.excel.type;

import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 * Created by https://github.com/kuangcp on 18-3-12  下午7:21
 *
 * @author kuangcp
 */
public class DateHandler implements LoadCellValue {

  @Override
  public Cell loadValue(Row row, int index, Object value) {
    Cell cell = row.createCell(index, CellType.STRING);
    cell.setCellValue((Date) value);
    return cell;
  }
}
