package com.github.kuangcp.kcpoi.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp on 18-2-27  下午12:32
 *
 * Excel的一般性导出配置
 *
 * @author kuangcp
 */
@Data
@Slf4j
public class MainConfig {

  /**
   * 列起始位置 0 1 2 ...
   */
  private int startColNum = 0;

  /**
   * 行起始位置 0 1 2 ...
   */
  private int startRowNum = 0;

  /**
   * 合并的大标题的最后一行下标 0 表示不展示合并标题
   */
  private int titleLastRowNum = 1;

  /**
   * Excel 内容的起始行数 和Excel中行数一致(从0开始) 一般这个是 titleLastRowNum +1 , 因为有一行是列标题, 但是也可用来自定义别的行进来
   */
  private int contentStartNum = 2;

  /**
   * Excel 中所有日期的格式
   */
  private String dateFormat = "yyyy-MM-dd";

  private short titleFontSize = 12;
  private String titleFontName = "Courier New";
  private boolean isTitleFontBold = true;

  private short contentFontSize = 10;
  private String contentFontName = "Arial";
  private boolean isContentFontBold = false;
}
