package com.github.kuangcp.kafka;

import com.github.kuangcp.kafka.common.MessageExecutor;
import com.github.kuangcp.kafka.common.SimpleMessageExecutor;
import com.github.kuangcp.kafka.domain.Topics;
import com.github.kuangcp.kafka.executor.HiExecutor;
import com.github.kuangcp.kafka.executor.LoginMessageExecutor;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-11-24 00:24
 */
public class KafkaConsumerUtilTest {

  @Test
  public void testConsumerWithSimpleExecutor() {
    HiExecutor executor1 = new HiExecutor("executor-1");

    KafkaConsumerUtil.consumerPlainText(Duration.ofMillis(1000),
        Collections.singletonList(executor1));

    HiExecutor executor2 = new HiExecutor("executor-2");
    KafkaConsumerUtil.consumerPlainText(Duration.ofMillis(1000),
        Collections.singletonList(executor2));

    try {
      Thread.currentThread().join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMultiple() {
    SimpleMessageExecutor oneExecutor = new SimpleMessageExecutor() {
      @Override
      public void execute(String message) {

        long receive = System.nanoTime();
        System.out.println(
            "one: " + message + " waste:" + (receive - Long.parseLong(message)) / 1000000 + "ms");
      }

      @Override
      public String getTopic() {
        return Topics.ONE;
      }
    };

    SimpleMessageExecutor twoExecutor = new SimpleMessageExecutor() {
      @Override
      public void execute(String message) {
        long receive = System.nanoTime();
        System.out.println(
            "two: " + message + " waste:" + (receive - Long.parseLong(message)) / 1000000 + "ms");
      }

      @Override
      public String getTopic() {
        return Topics.TWO;
      }
    };

    KafkaConsumerUtil.consumerPlainText(Arrays.asList(oneExecutor, twoExecutor));
    KafkaConsumerUtil.consumerPlainText(Arrays.asList(oneExecutor, twoExecutor));
  }

  @Test
  public void testConsumerMessage() throws InterruptedException {
    LoginMessageExecutor executor = new LoginMessageExecutor();
    KafkaConsumerUtil.consumer(executor);
    block(5000);
  }

  @Test
  public void testConsumer() throws Exception {
    KafkaConsumerUtil.consumerPlainText(new MessageExecutor<String>() {
      @Override
      public void execute(String message) {
        System.out.println(message);
      }

      @Override
      public String getTopic() {
        return "OFC_REVERSE_DATA_TRACK";
      }
    });

    block(5000);
  }

  private void block(long millis) throws InterruptedException {
    Thread.currentThread().join(millis);
  }
}
