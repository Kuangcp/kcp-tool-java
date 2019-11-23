package com.github.kuangcp.kafka;

import com.github.kuangcp.kafka.executor.HiExecutor;
import com.github.kuangcp.kafka.executor.LoginMessageExecutor;
import java.time.Duration;
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
  public void testConsumerMessage(){
    LoginMessageExecutor executor = new LoginMessageExecutor();
    KafkaConsumerUtil.consumer(Duration.ofMillis(1000), Collections.singletonList(executor));
  }
}
