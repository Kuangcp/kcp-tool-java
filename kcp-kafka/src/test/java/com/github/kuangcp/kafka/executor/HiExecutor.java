package com.github.kuangcp.kafka.executor;

import com.github.kuangcp.kafka.common.SimpleMessageExecutor;
import com.github.kuangcp.kafka.domain.Topics;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-11-22 21:16
 */
@Slf4j
public class HiExecutor implements SimpleMessageExecutor {

  @Override
  public void execute(String message) {
    log.info(": message={}", message);
  }

  @Override
  public String getTopic() {
    return Topics.HI;
  }
}
