package com.github.kuangcp.kafka.executor;

import com.github.kuangcp.kafka.common.GeneralMessageExecutor;
import com.github.kuangcp.kafka.common.Message;
import com.github.kuangcp.kafka.domain.Topics;
import com.github.kuangcp.kafka.domain.User;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-11-23 21:51
 */
@Slf4j
public class LoginMessageExecutor implements GeneralMessageExecutor<User> {

  @Override
  public void execute(Message<User> message) {
    log.warn("user={} msg={}", message.getContent(), message);
  }

  @Override
  public String getTopic() {
    return Topics.USER_LOGIN;
  }

  @Override
  public Class<User> getContentClass() {
    return User.class;
  }
}
