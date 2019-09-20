package com.github.kuangcp.json;

import java.util.Objects;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-06-13 22:55
 */
@Slf4j
public class InitObjectByClassTest {

  @Test
  public void testFillAllFieldValue() throws Exception {
    String string = InitObjectByClass.getString(Computer.class, Objects::toString);
    log.info(string);
  }
}

@Data
abstract class StandardIO {

  public String input;
  public String output;
}

@Data
@EqualsAndHashCode(callSuper = true)
class KeyBoard extends StandardIO {

  private String brandName;
  private Integer keyNum;
  private double price;
}

@Data
class Computer {

  private String name;

  private KeyBoard keyBoard;
}