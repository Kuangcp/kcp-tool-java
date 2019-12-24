package com.github.kuangcp.kafka;

import com.github.kuangcp.kafka.common.EnhanceMessageExecutor;
import com.github.kuangcp.kafka.common.Message;
import com.github.kuangcp.kafka.domain.Topics;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-12-24 21:22
 */
public class KafkaConsumerMultipleTest {

  @Test
  public void testConsumer() throws InterruptedException {
    EnhanceMessageExecutor<String> enhanceMessageExecutor = new EnhanceMessageExecutor<String>() {
      @Override
      public Class getContentClass() {
        return String.class;
      }

      @Override
      public void execute(Message<String> message) {
        System.out.println(message);
      }

      @Override
      public String getTopic() {
        return Topics.HI;
      }

      @Override
      public ThreadPoolExecutor getConsumerThreadPool() {
        return new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
      }

    };

    KafkaConsumerMultiple.consumer(Collections.singletonList(enhanceMessageExecutor));

    Thread.currentThread().join();

  }
}