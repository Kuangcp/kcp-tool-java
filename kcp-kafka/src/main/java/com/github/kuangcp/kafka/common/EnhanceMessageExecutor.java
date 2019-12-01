package com.github.kuangcp.kafka.common;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author https://github.com/kuangcp on 2019-11-25 20:29
 */
public interface EnhanceMessageExecutor<T> extends GeneralMessageExecutor<Message<T>> {

  /**
   * 获取topic消费者的线程池
   */
  default ThreadPoolExecutor getConsumerThreadPool() {
    return null;
  }
}
