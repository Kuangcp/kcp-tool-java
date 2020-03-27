package com.github.kuangcp.kcpoi.excel;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import com.github.kuangcp.kcpoi.utils.Employee;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 18-2-21  下午10:10
 *
 * @author kuangcp
 */
@Ignore
@Slf4j
public class ExcelExportTest {

  private final String filePath = "/tmp/employee.xls";

  private List<Employee> buildData() {
    List<Employee> originList = new ArrayList<>();
    for (int i = 0; i < 500; i++) {
      Employee employee = Employee.builder()
          .QQ("QQ" + i)
          .name("name" + i)
          .phone("phone" + i)
          .sex("sex" + i)
          .age(i)
          .email("email" + i)
          .birth(new Date())
          .active(false)
          .build();
      originList.add(employee);
    }
    return originList;
  }

  @Test
  public void testExports() {
    Workbook workbook = new HSSFWorkbook();
    boolean results = ExcelExport.exportExcel(workbook, filePath, buildData());
    assertThat(results, equalTo(true));
  }

  @Test
  public void testOut() {
    Workbook workbook = new SXSSFWorkbook();
    File f = new File(filePath);
    OutputStream out = null;
    try {
      out = new FileOutputStream(f);
    } catch (FileNotFoundException e) {
      log.error("", e);
    }
    boolean results = ExcelExport.exportExcel(workbook, out, buildData());
    assertThat(results, equalTo(true));
  }
}