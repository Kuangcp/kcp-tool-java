package com.github.kuangcp.kafka.common;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author https://github.com/kuangcp on 2019-11-25 20:29
 */
public interface EnhanceMessageExecutor<T> extends MessageExecutor<Message<T>> {

  Class<T> getContentClass();

  /**
   * 获取topic消费者的线程池
   * @return
   */
  default ThreadPoolExecutor getConsumerThreadPool() {return null;}
}
