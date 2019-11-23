package com.github.kuangcp.kafka.config;

import java.util.Optional;
import java.util.Properties;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-11-24 00:21
 */
public class KafkaConfigManagerTest {

  @Test
  public void testConsumer(){
    Optional<Properties> propOpt = KafkaConfigManager.getConsumerConfig();

    propOpt.ifPresent(System.out::println);
  }
}
