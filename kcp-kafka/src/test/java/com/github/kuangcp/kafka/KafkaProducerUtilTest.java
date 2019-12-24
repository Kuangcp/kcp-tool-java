package com.github.kuangcp.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.kafka.domain.Book;
import com.github.kuangcp.kafka.domain.Topics;
import com.github.kuangcp.kafka.domain.User;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import lombok.Builder;
import lombok.Data;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-11-24 00:23
 */
public class KafkaProducerUtilTest {


  @Test
  public void testSendPlainText() {
    KafkaProducerUtil.sendAsPlainText(Topics.HI, "test send message");
    for (int i = 0; i < 2; i++) {
      KafkaProducerUtil.sendAsPlainText(Topics.HI, "" + System.nanoTime());
//      KafkaProducerUtil.sendPlainText(Topics.TWO, "" + System.nanoTime());
    }
  }

  @Data
  @Builder
  static class TestData {
    private String row;
    @JsonProperty("event_type")
    private String eventType;
    private String body;
  }

  @Test
  public void testLogStash() {
    TestData data = TestData.builder()
        .row("1_13")
        .eventType("1")
        .body("FSFS" + LocalDateTime.now().toString())
        .build();

    KafkaProducerUtil.send(Topics.HI, data);
  }

  @Test
  public void testJSON() throws JsonProcessingException {
    TestData data = TestData.builder()
        .row("1_7")
        .eventType("1")
        .body("FSFS" + LocalDateTime.now().toString())
        .build();

    ObjectMapper objectMapper = new ObjectMapper();
    String result = objectMapper.writeValueAsString(data);
    System.out.println(result);
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
