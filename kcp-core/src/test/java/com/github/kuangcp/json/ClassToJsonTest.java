package com.github.kuangcp.json;

import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp
 * @date 2019-06-13 22:55
 */
public class ClassToJsonTest {

  @Test
  public void testFillAllFieldValue() throws Exception {
    Optional<Computer> computerOpt = ClassToJson.fillAllFieldValue(Computer.class);
    computerOpt.ifPresent(System.out::println);
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