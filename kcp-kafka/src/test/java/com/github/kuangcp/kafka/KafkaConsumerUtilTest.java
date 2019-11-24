package com.github.kuangcp.kafka;

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
    HiExecutor executor = new HiExecutor();

    KafkaConsumerUtil.consumerPlainText(Duration.ofMillis(1000),
        Collections.singletonList(executor));
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

    KafkaConsumerUtil.consumerPlainText(Duration.ofMillis(1000),
        Arrays.asList(oneExecutor, twoExecutor));

    KafkaConsumerUtil.consumerPlainText(Duration.ofMillis(3000),
        Arrays.asList(oneExecutor, twoExecutor));
  }

  @Test
  public void testConsumerMessage() {
    LoginMessageExecutor executor = new LoginMessageExecutor();
    KafkaConsumerUtil.consumer(Duration.ofMillis(1000), Collections.singletonList(executor));
  }
}
