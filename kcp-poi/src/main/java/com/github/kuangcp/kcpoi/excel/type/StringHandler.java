package com.github.kuangcp.kcpoi.excel.type;

import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 * Created by https://github.com/kuangcp on 18-3-12  上午11:16
 *
 * @author kuangcp
 */
public class StringHandler implements LoadCellValue {

  @Override
  public Cell loadValue(Row row, int index, Object value) {
    Cell cell = row.createCell(index, CellType.STRING);
    cell.setCellValue(Optional.ofNullable(value).map(Object::toString).orElse(null));
    return cell;
  }
}
