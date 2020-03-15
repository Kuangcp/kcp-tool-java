package com.github.kuangcp.kcpoi.excel;

import com.github.kuangcp.kcpoi.excel.domain.DataBlock;
import com.github.kuangcp.kcpoi.excel.util.ReadAnnotationUtil;
import com.github.kuangcp.kcpoi.utils.Employee;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-12-13 19:45
 */
@Slf4j
public class ExcelWriterTest {

  private String filePath = "/tmp/employee-mul.xls";
  private List<List<Employee>> originList = new ArrayList<>();
  private int block = 3;
  private int blockLength = 50;

  @Before
  public void init() {
    for (int j = 0; j < block; j++) {
      List<Employee> temp = new ArrayList<>();
      for (int i = 0; i < blockLength; i++) {
        int id = j * blockLength + i;
        Employee employee = Employee.builder()
            .QQ("QQ" + id)
            .name("name" + id)
            .phone("phone" + id)
            .sex("sex" + id)
            .age(id)
            .email("email" + id)
            .birth(new Date())
            .active(false)
            .build();
        temp.add(employee);
      }
      originList.add(temp);
    }
  }

  // 开始的倒数锁
  final CountDownLatch begin = new CountDownLatch(1);

  // 结束的倒数锁
  final CountDownLatch end = new CountDownLatch(block);
  ExecutorService executorService = Executors.newSingleThreadExecutor();

  @Test
  public void testExport() {
    Workbook workbook = new SXSSFWorkbook();
    String sheetTitle = ReadAnnotationUtil.getSheetExportTitle(Employee.class);

    Sheet sheet = workbook.createSheet(sheetTitle);
    int start = 0;
    List<DataBlock<Employee>> blocks = new ArrayList<>();
    for (List<Employee> employees : originList) {
      DataBlock<Employee> block = DataBlock.<Employee>builder()
          .startNum(start)
          .endNum(start + blockLength - 1)
          .data(employees)
          .build();
      start += blockLength;

      blocks.add(block);
    }

    for (DataBlock<Employee> block : blocks) {
      executorService.submit(() -> {
        try {
          begin.await();
          boolean fillResult = ExcelWriter.fillData(sheet, block);
          log.info("fillResult={}", fillResult);
        } catch (Exception e) {
          log.error("", e);
        } finally {
          end.countDown();
        }
      });
    }

    begin.countDown();
    log.info("start export");
    try {
      end.await();
      log.info("complete fill data");
      File file = new File(filePath);
      OutputStream out = new FileOutputStream(file);
      workbook.write(out);
    } catch (Exception e) {
      log.error("", e);
    }
  }
}
