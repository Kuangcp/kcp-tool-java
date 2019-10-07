package com.github.kuangcp.aop.represented;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-10-06 18:42
 */
@Slf4j
public class Searcher {

  public String search(String... word) {
    log.info("word={}", Arrays.toString(word));
    return String.valueOf(System.currentTimeMillis());
  }
}
