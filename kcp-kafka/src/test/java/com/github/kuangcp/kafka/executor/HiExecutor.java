package com.github.kuangcp.kafka.executor;

import com.github.kuangcp.kafka.common.SimpleMessageExecutor;
import com.github.kuangcp.kafka.domain.Topics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-11-22 21:16
 */
@Data
@Slf4j
@AllArgsConstructor
public class HiExecutor implements SimpleMessageExecutor {

  private String name;

  @Override
  public void execute(String message) {
    log.info("{}: message={}", name, message);
  }

  @Override
  public String getTopic() {
    return Topics.HI;
  }
}
