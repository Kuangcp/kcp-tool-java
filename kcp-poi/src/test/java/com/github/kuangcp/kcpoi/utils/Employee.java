package com.github.kuangcp.kcpoi.utils;

import com.github.kuangcp.kcpoi.excel.base.ExcelTransform;
import com.github.kuangcp.kcpoi.excel.base.ExcelConfig;
import com.github.kuangcp.kcpoi.excel.base.ExcelSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ExcelSheet(exportTitle = "雇员表", importTitle = "雇员表")
public class Employee implements ExcelTransform {

  @ExcelConfig("姓名")
  private String name;

  @ExcelConfig("性别")
  private String sex;

  @ExcelConfig("年龄")
  private int age;

  @ExcelConfig("生日")
  private Date birth;

  @ExcelConfig("活跃")
  private Boolean active;

  @ExcelConfig("分数")
  private double score;

  @ExcelConfig("联系电话")
  private String phone;

  @ExcelConfig("QQ号码")
  private String QQ;

  @ExcelConfig("邮箱")
  private String email;

  @ExcelConfig("field1")
  private String field1 = "";
  @ExcelConfig("field2")
  private String field2 = "";
  @ExcelConfig("field3")
  private String field3 = "";
  @ExcelConfig("field4")
  private String field4 = "";
  @ExcelConfig("field5")
  private String field5 = "";
}