package com.github.kuangcp.kafka;

import static java.util.stream.Collectors.toList;

import com.github.kuangcp.kafka.common.EnhanceMessageExecutor;
import com.github.kuangcp.kafka.common.Message;
import com.github.kuangcp.kafka.config.KafkaConfigManager;
import java.lang.reflect.Array;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

/**
 * @author https://github.com/kuangcp on 2019-11-25 20:17
 */
@Slf4j
public final class KafkaConsumerMultiple {

  private Properties consumerConfig;

  /**
   * 每个topic的消息要分成多少个消息队列(stream),方便并发处理
   */
  @Setter
  private int threadPerTopic = 1;

  /**
   * 分发线程池 存放阻塞的 consumer.poll 进程
   */
  private ExecutorService dispatchExecutor;

  /**
   * 默认消息业务线程池 存放实际业务执行线程，当处理器存在自定义线程池时，该线程池不生效
   */
  private ExecutorService handlerExecutor;

  /**
   * 默认分发线程数量
   */
  private static final int DEFAULT_DISPATCH_THREAD_NUM = 2;

  // TODO
  private List<EnhanceMessageExecutor> enhanceMessageExecutors;

  /**
   * 分发线程句柄
   */
  private final Set<TopicDispatchRunner> runners = new HashSet<>();

//  @Override
  public void afterPropertiesSet() {
    this.consumerConfig =  KafkaConfigManager.getConsumerConfig()
        .orElseThrow(() -> new RuntimeException("加载Kafka消费者配置出错"));

    // 该方法用来在jvm中增加一个关闭的钩子。
    // 当程序正常退出,系统调用 System.exit方法或虚拟机被关闭时才会执行添加的shutdownHook线程。
    // 其中shutdownHook是一个已初始化但并不有启动的线程，
    // 当jvm关闭的时候，会执行系统中已经设置的所有通过方法addShutdownHook添加的钩子，当系统执行完这些钩子后，jvm才会关闭。
    // 所以可通过这些钩子在jvm关闭的时候进行内存清理、资源回收等工作。
    Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

    initConsumeMsg();
  }

  public static boolean isNoneBlank(final CharSequence... css) {
    return !isAnyBlank(css);
  }
  public static boolean isAnyBlank(final CharSequence... css) {
    if (isEmpty(css)) {
      return false;
    }
    for (final CharSequence cs : css){
      if (isBlank(cs)) {
        return true;
      }
    }
    return false;
  }
  public static boolean isBlank(final CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }
  public static boolean isEmpty(final Object[] array) {
    return getLength(array) == 0;
  }
  public static int getLength(final Object array) {
    if (array == null) {
      return 0;
    }
    return Array.getLength(array);
  }

  public static boolean isEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  private void initConsumeMsg() {
    // 获取 topic 配置
    List<EnhanceMessageExecutor> hasEnhanceMessageExecutors = Optional.ofNullable(
        enhanceMessageExecutors)
        .orElse(Collections.emptyList()).stream()
        .filter(m -> isNoneBlank(m.getTopic()))
        .collect(toList());
    if (isEmpty(hasEnhanceMessageExecutors)) {
      log.info("hasEnhanceMessageExecutors is zero!");
      return;
    }

    Map<String, List<EnhanceMessageExecutor>> map = hasEnhanceMessageExecutors.stream()
        .collect(Collectors.groupingBy(EnhanceMessageExecutor::getTopic));
    Set<String> topics = map.keySet();

    //创建线程池
    int dispatchThreadCount = Optional.ofNullable(1)
        .orElse(DEFAULT_DISPATCH_THREAD_NUM);
    if (dispatchThreadCount <= 0) {
      dispatchThreadCount = DEFAULT_DISPATCH_THREAD_NUM;
    }
    dispatchExecutor = this.buildTopicExecutor(
        dispatchThreadCount,
        "message-dispatcher"
    );
    handlerExecutor = this.buildTopicExecutor(
        topics.size() * threadPerTopic,
        "message-handler"
    );

    // 创建分发线程
    List<String> topicList = new ArrayList<>(topics);
    List<List<String>> topicPartitions = this.partition(topicList, dispatchThreadCount);
    topicPartitions.forEach(partition -> this.createDispatchThread(partition, map));
  }

  private List<List<String>> partition(List<String> list, int splitCount) {
    List<List<String>> result = new ArrayList<>();

    int size = list.size();
    int partitionSize = size / splitCount;
    boolean hasTail = size % splitCount > 0;

    for (int i = 0; i < splitCount; i++) {
      int startIndex = i * partitionSize;
      int endIndex = i * partitionSize + partitionSize;
      result.add(new ArrayList<>(list.subList(startIndex, endIndex)));
    }

    if (hasTail) {
      List<String> last = result.get(result.size() - 1);
      last.addAll(list.subList(splitCount * partitionSize, list.size()));
    }

    return result;
  }

  private ExecutorService buildTopicExecutor(int threadCount, String threadName) {
    return new ThreadPoolExecutor(threadCount, threadCount, 0, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(),
        new TopicThreadFactory(threadName),
        new ThreadPoolExecutor.AbortPolicy());
  }

  private void createDispatchThread(List<String> topics,
      Map<String, List<EnhanceMessageExecutor>> topic2MessageExecutorsMap) {
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig);
    TopicDispatchRunner runner = new TopicDispatchRunner(topics, consumer, handlerExecutor, topic2MessageExecutorsMap);
    runners.add(runner);
    dispatchExecutor.execute(runner);
  }

  private void shutdown() {
    try {
      runners.forEach(TopicDispatchRunner::shutdown);
      dispatchExecutor.shutdown();
      runners.clear();
    } catch (Exception e) {
      log.error("shutdown messageConsumer and ThreadPool error", e);
    }
  }

  static class TopicDispatchRunner implements Runnable {
    @Getter
    private List<String> topics;
    private KafkaConsumer<String, String> consumer;
    private Map<String, List<EnhanceMessageExecutor>> topicExecutorMapping;
    private ExecutorService workers;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    TopicDispatchRunner(List<String> topics,
        KafkaConsumer<String, String> consumer,
        ExecutorService workers,
        Map<String, List<EnhanceMessageExecutor>> topicExecutorMapping) {
      this.topics = topics;
      this.consumer = consumer;
      this.topicExecutorMapping = topicExecutorMapping;
      this.workers = workers;
    }

    @Override
    public void run() {
      try {
        log.info("Topic dispatcher subscribe topics :{} ", topics);
        this.consumer.subscribe(topics);
        while (!closed.get()) {
          try {
            dispatch();
          } catch (WakeupException e) {
            // 中断信号，直接退出
            throw e;
          } catch (Throwable others) {
            // 未知异常，捕获记录后重试
            log.error("Something goes wrong with topic - {} ... please check consumer logs ..", this.topics);
            log.error(others.getMessage(), others);
          }
        }
      } catch (WakeupException e) {
        // Ignore exception if closing
        if (!closed.get()) {
          throw e;
        }
      } finally {
        consumer.close();
      }
    }

    private void dispatch() {
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
      for (ConsumerRecord<String, String> record : records) {
        Message message = this.parseMessage(record);
        String topic = record.topic();
        Collection<EnhanceMessageExecutor> messageHandlers = topicExecutorMapping.get(topic);
        for (EnhanceMessageExecutor handler : messageHandlers) {
          ExecutorService workers;
          if (handler.getConsumerThreadPool() != null) {
            workers = handler.getConsumerThreadPool();
          } else {
            workers = this.workers;
          }
          workers.execute(() -> {
            log.info("[consumer-{}-{}]: Topic :{} Message: {}",
                Thread.currentThread().getName(),
                Thread.currentThread().getId(),
                topic, record.value());
            handler.execute(message);
            log.info("[consumer-{}-{}]: Consume message from topic :{} Message: {} Done.",
                Thread.currentThread().getName(),
                Thread.currentThread().getId(),
                topic, record.value());
          });
        }

      }
    }

    // Shutdown hook which can be called from a separate thread
    void shutdown() {
      closed.set(true);
      consumer.wakeup();
    }

    private Message parseMessage(ConsumerRecord<String, String> record) {
      return null;
//      return JSON.parseObject(record.value(), Message.class, Feature.SortFeidFastMatch);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      TopicDispatchRunner runner = (TopicDispatchRunner) o;
      return consumer.equals(runner.consumer);
    }

    @Override
    public int hashCode() {
      return Objects.hash(consumer);
    }
  }

  static class TopicThreadFactory implements ThreadFactory {
    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    TopicThreadFactory(String poolName) {
      SecurityManager s = System.getSecurityManager();
      group = (s != null) ? s.getThreadGroup() :
          Thread.currentThread().getThreadGroup();
      namePrefix = "pool-" + poolName + "-" +
          POOL_NUMBER.getAndIncrement() +
          "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
      Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
      if (t.isDaemon()) {
        t.setDaemon(false);
      }
      if (t.getPriority() != Thread.NORM_PRIORITY) {
        t.setPriority(Thread.NORM_PRIORITY);
      }
      return t;
    }
  }

}
