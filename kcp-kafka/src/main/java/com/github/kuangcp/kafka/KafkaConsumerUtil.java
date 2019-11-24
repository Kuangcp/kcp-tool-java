package com.github.kuangcp.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.kafka.common.GeneralMessageExecutor;
import com.github.kuangcp.kafka.common.Message;
import com.github.kuangcp.kafka.common.MessageExecutor;
import com.github.kuangcp.kafka.common.MessageTopic;
import com.github.kuangcp.kafka.config.KafkaConfigManager;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author https://github.com/kuangcp on 2019-11-13 09:35
 */
@Slf4j
@SuppressWarnings("unchecked")
public class KafkaConsumerUtil {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private static volatile boolean stop = false;

  private static ExecutorService pool = Executors
      .newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  private static Properties properties = KafkaConfigManager.getConsumerConfig()
      .orElseThrow(() -> new RuntimeException("加载Kafka消费者配置出错"));

  private static KafkaConsumer consumer;

  static {
    Runtime.getRuntime().addShutdownHook(new Thread(KafkaConsumerUtil::shutdown));
  }

  private KafkaConsumerUtil() {

  }

  // TODO 两个线程分摊 Topics？

  /**
   * 消费Topic
   *
   * @param duration 拉取间隔
   * @param executors 执行器
   * @param <E> executor
   */
  public static <E extends MessageExecutor<String> & MessageTopic> void consumerPlainText(
      Duration duration, Collection<E> executors) {
    if (!validate(duration, executors)) {
      return;
    }

    init();

    Map<String, E> executorMap = executors.stream()
        .collect(Collectors.toMap(MessageExecutor::getTopic, Function.identity(),
            (front, current) -> current));

    consumer.subscribe(executorMap.keySet());
    while (!stop) {
      ConsumerRecords<String, String> records = consumer.poll(duration);
      for (ConsumerRecord<String, String> record : records) {
        E executor = executorMap.get(record.topic());
        if (Objects.nonNull(executor)) {
          pool.submit(() -> executor.execute(record.value()));
        }
      }
    }
  }

  private static boolean validate(Duration duration, Collection executors) {
    if (Objects.isNull(duration) || Objects.isNull(executors) || executors.isEmpty()) {
      log.warn("consumer param invalid");
      return false;
    }

    if (Objects.nonNull(consumer)) {
      log.warn("consumer has started");
      return false;
    }

    return true;
  }

  /**
   * 消费Topic
   *
   * @param duration 拉取间隔
   * @param executors 执行器
   * @param <T> Message content 类型
   */
  public static <T> void consumer(Duration duration,
      Collection<GeneralMessageExecutor<T>> executors) {
    if (!validate(duration, executors)) {
      return;
    }

    init();

    Map<String, GeneralMessageExecutor<T>> executorMap = executors.stream()
        .collect(Collectors.toMap(GeneralMessageExecutor::getTopic, Function.identity(),
            (front, current) -> current));

    consumer.subscribe(executorMap.keySet());
    while (!stop) {
      ConsumerRecords<String, String> records = consumer.poll(duration);
      for (ConsumerRecord<String, String> record : records) {
        GeneralMessageExecutor<T> executor = executorMap.get(record.topic());
        if (Objects.isNull(executor)) {
          continue;
        }

        pool.submit(() -> {
          try {
            Message<T> message = objectMapper.readValue(record.value(), Message.class);

            // 因为以上反序列化无法获取泛型的类型，所以无法正确的设置 content 对象，根本原因是Java的泛型擦除
            // 解决方案是先将content再序列化 最后指定类型反序列化
            Class<T> contentClass = executor.getContentClass();
            String value = objectMapper.writeValueAsString(message.getContent());
            T content = objectMapper.readValue(value, contentClass);
            message.setContent(content);

            executor.execute(message);
          } catch (Exception e) {
            log.error("", e);
          }
        });
      }
    }
  }

  /**
   * 消费者资源回收 线程池关闭
   */
  private static void shutdown() {
    stop = true;
    try {
      if (Objects.nonNull(consumer)) {
        consumer.close();
      }
      pool.shutdown();
    } catch (Exception e) {
      log.error("shutdown messageProducer error");
    }
  }

  private synchronized static void init() {
    if (Objects.isNull(consumer)) {
      consumer = new KafkaConsumer<>(properties);
    }
  }
}
