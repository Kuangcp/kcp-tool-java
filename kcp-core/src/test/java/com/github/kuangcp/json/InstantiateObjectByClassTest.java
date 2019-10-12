package com.github.kuangcp.json;

import java.math.BigDecimal;
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
public class InstantiateObjectByClassTest {

  @Test
  public void testToString() {
    String string = InstantiateObjectByClass.fillFieldsThenToString(Computer.class);
    log.info(string);
  }

  @Test
  public void testFillFields(){
    Optional<Computer> computerOpt = InstantiateObjectByClass.fillFields(Computer.class);
    if (computerOpt.isPresent()) {
      Computer computer = computerOpt.get();
      System.out.println(computer);
    }
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
  private BigDecimal realPrice;
}

@Data
class Computer {

  private String name;

  private KeyBoard keyBoard;
}