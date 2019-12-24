package com.github.kuangcp.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.kafka.common.Message;
import com.github.kuangcp.kafka.config.KafkaConfigManager;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:36
 */
@Slf4j
public final class KafkaProducerUtil {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private static Properties properties = KafkaConfigManager.getProducerConfig()
      .orElseThrow(() -> new RuntimeException("加载Kafka生产者配置出错"));

  private static Producer<String, String> producer;

  static {
    Runtime.getRuntime().addShutdownHook(new Thread(KafkaProducerUtil::shutdown));
  }

  private KafkaProducerUtil() {
  }

  /**
   * 发送消息
   *
   * @param topic 主题
   * @param message 消息
   * @param <T> 内容类型
   * @see Message
   */
  public static <T> void send(String topic, Message<T> message) {
    initKafkaProducer();

    if (Objects.isNull(topic) || topic.isEmpty() || Objects.isNull(message)) {
      return;
    }

    try {
      String messageStr = objectMapper.writeValueAsString(message);
      producer.send(new ProducerRecord<>(topic, messageStr));
    } catch (JsonProcessingException e) {
      log.error("", e);
    }
  }

  /**
   * 发送消息
   *
   * @param topic 主题
   * @param content 消息内容
   * @param <T> Message 中 内容类型
   * @see Message
   */
  public static <T> void send(String topic, T content) {
    initKafkaProducer();

    Message<T> message = new Message<>(content);
    try {
      String messageStr = objectMapper.writeValueAsString(message);

      sendAsPlainText(topic, messageStr);
    } catch (JsonProcessingException e) {
      log.error("", e);
    }
  }

  /**
   * 发送纯字符串消息
   *
   * @param topic 主题
   * @param message 消息
   */
  public static void sendAsPlainText(String topic, String message) {
    initKafkaProducer();

    producer.send(new ProducerRecord<>(topic, message));
    log.info("send message succeed: message={}", message);
  }

  private synchronized static void initKafkaProducer() {
    if (Objects.isNull(producer)) {
      producer = new KafkaProducer<>(properties);
    }
  }

  /**
   * 发送消息
   *
   * @param topic 主题
   * @param content 消息内容对象 将转为JSON串进行发送
   * @param <T> 消息内容类型
   */
  public static <T> void sendAsJSON(String topic, T content) {
    initKafkaProducer();

    try {
      String messageStr = objectMapper.writeValueAsString(content);

      sendAsPlainText(topic, messageStr);
    } catch (JsonProcessingException e) {
      log.error("", e);
    }
  }

  /**
   * 生产者资源回收 未发送的消息进行发送
   */
  private static void shutdown() {
    try {
      producer.close();
    } catch (Exception e) {
      log.error("shutdown messageProducer error");
    }
  }
}
