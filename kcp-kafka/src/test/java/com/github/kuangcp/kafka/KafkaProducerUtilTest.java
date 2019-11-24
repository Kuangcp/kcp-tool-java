package com.github.kuangcp.kafka;

import com.github.kuangcp.kafka.domain.Book;
import com.github.kuangcp.kafka.domain.Topics;
import com.github.kuangcp.kafka.domain.User;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-11-24 00:23
 */
public class KafkaProducerUtilTest {


  @Test
  public void testSendPlainText() {
//    KafkaProducerUtil.sendPlainText(Topics.HI, "test send message");
    for (int i = 0; i < 100; i++) {
      KafkaProducerUtil.sendPlainText(Topics.ONE, "" + System.nanoTime());
      KafkaProducerUtil.sendPlainText(Topics.TWO, "" + System.nanoTime());
    }
  }

  @Test
  public void testSendMessage() {
    Book book = Book.builder().name("test").type("math").build();
    User user = User.builder().name("one").nickName("two")
        .phones(Arrays.asList("11111", "22222"))
        .books(Collections.singletonList(book))
        .build();
    KafkaProducerUtil.send(Topics.USER_LOGIN, user);
  }
}
