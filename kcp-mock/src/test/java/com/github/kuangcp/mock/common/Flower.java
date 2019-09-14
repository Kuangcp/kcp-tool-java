package com.github.kuangcp.mock.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author https://github.com/kuangcp on 2019-09-14 14:05
 */
@Data
@NoArgsConstructor
class Flower {

  private String bud;
  private String pollen;
  private Petal petal;
}
