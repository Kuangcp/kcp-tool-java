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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 18-2-21  下午10:10
 *
 * @author kuangcp
 */
@Ignore
public class ExcelExportTest {

  private String filePath = "/tmp/employee.xls";
  private List<Employee> originList = new ArrayList<>();

  @Before
  public void init() {
    for (int i = 0; i < 50000; i++) {
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
  }

  @Test
  public void testExports() {
    Workbook workbook = new HSSFWorkbook();
    boolean results = ExcelExport.exportExcel(workbook, filePath, originList);
    assertThat(results, equalTo(true));
  }

  @Test
  public void testOut() {
    Workbook workbook = new HSSFWorkbook();
    File f = new File(filePath);
    OutputStream out = null;
    try {
      out = new FileOutputStream(f);
    } catch (FileNotFoundException e11) {
      e11.printStackTrace();
    }
    boolean results = ExcelExport.exportExcel(workbook, out, originList);
    assertThat(results, equalTo(true));
  }
}