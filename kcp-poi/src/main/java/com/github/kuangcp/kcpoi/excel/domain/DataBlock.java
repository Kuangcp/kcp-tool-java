package com.github.kuangcp.kcpoi.excel.domain;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author https://github.com/kuangcp on 2019-12-13 19:56
 */
@Data
@Builder
public class DataBlock<T> {

  private int startNum;

  private int endNum;

  private List<T> data;

  public boolean isEmpty(){
    return CollectionUtils.isEmpty(data);
  }
}
