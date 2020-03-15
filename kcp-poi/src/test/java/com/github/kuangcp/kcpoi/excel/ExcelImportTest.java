package com.github.kuangcp.kcpoi.excel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.kcpoi.utils.Employee;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 18-2-23  下午9:54
 *
 * @author kuangcp
 */
@Ignore
@Slf4j
public class ExcelImportTest {

  private String filePath = "/home/kcp/test/employee.xls";

  @Test
  public void testImportExcel() {

    List<Employee> result = ExcelImport.importExcel(filePath, Employee.class);
    Assert.assertNotNull(result);
    result.forEach(item -> System.out.println(item.toString()));

    ObjectMapper mapper = new ObjectMapper();
    try {
      String json = mapper.writeValueAsString(result);
      log.debug("result : json={}", json);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testStreamImport() throws FileNotFoundException {
    FileInputStream inputStream = new FileInputStream(filePath);
    List<Employee> result = ExcelImport.importExcel(inputStream, Employee.class);

    Assert.assertNotNull(result);

    result.forEach(item -> System.out.println(item.toString()));
  }


}

