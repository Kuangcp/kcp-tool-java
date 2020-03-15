package com.github.kuangcp.kcpoi.utils.base;

import com.github.kuangcp.kcpoi.excel.ExcelCellMeta;
import com.github.kuangcp.kcpoi.excel.base.ExcelConfig;
import com.github.kuangcp.kcpoi.excel.base.ExcelTransform;
import com.github.kuangcp.kcpoi.excel.util.ReadAnnotationUtil;
import com.github.kuangcp.kcpoi.utils.Employee;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp on 18-2-22  下午4:20
 *
 * @author kuangcp
 */
@Slf4j
public class ReadAnnotationUtilTest {

  @Test
  public void testTransform() throws Exception {
    Employee e1 = new Employee();
    e1.setQQ("QQ1");
    e1.setName("Name1");
    e1.setPhone("Phone1");
    e1.setSex("sex1");
    e1.setEmail("email");

    Employee e2 = new Employee();
    e2.setName("name2");
    e2.setPhone("phone2");
    e2.setSex("sex2");
    e2.setQQ("QQ2");
    e2.setBirth(new Date());
    List<Employee> originList = new ArrayList<>();
    originList.add(e1);
    originList.add(e2);

    List<Object[]> result = ReadAnnotationUtil.getContentByList(Employee.class, originList);
    Assert.assertEquals(2, result.size());
    for (Object[] temp : result) {
      for (Object n : temp) {
        System.out.print(n + " ");
      }
      System.out.println();
    }
  }

  /**
   * 测试读取注解信息
   *
   * 注意: 注解在什么对象上就拿到该对应对象调用 isAnnotationPresent 方法即可判断是否有对应注解存在
   */
  @Test
  public void readConfig() {
    final Class<? extends ExcelTransform> cls = Employee.class;
    final Field[] fields = cls.getDeclaredFields();

    Assert.assertNotNull(fields);
    for (Field field : fields) {
      String value = null;
      if (field.isAnnotationPresent(ExcelConfig.class)) {
        ExcelConfig excelConfig = field.getAnnotation(ExcelConfig.class);
        value = excelConfig.value();
      }
      log.info("name={} value={}", field.getName(), value);
    }
  }

  @Test
  public void testReadCellMeta(){
    List<ExcelCellMeta> cellMetaData = ReadAnnotationUtil.getCellMetaData(Employee.class);
    cellMetaData.stream().forEach(System.out::println);
  }
}