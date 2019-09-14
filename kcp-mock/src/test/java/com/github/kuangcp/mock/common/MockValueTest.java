package com.github.kuangcp.mock.common;

import java.math.BigDecimal;
import java.util.Date;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-07-14 12:54
 */
public class MockValueTest {


  @Test
  public void testMock() {
    Integer mock = MockValue.mock(100);
    System.out.println(mock);

    Double value = MockValue.mock(30.0);
    System.out.println(value);

    String string = MockValue.mock("20");
    System.out.println(string);

    Date date = MockValue.mock(Date.class);
    System.out.println(date);

    BigDecimal price = MockValue.mock(BigDecimal.valueOf(3.4));
    System.out.println(price);
  }

  @Test
  public void testMockByType() {
    System.out.println(MockValue.mock(String.class));
    System.out.println(MockValue.mock(int.class));
    System.out.println(MockValue.mock(double.class));
    System.out.println(MockValue.mock(long.class));
    System.out.println(MockValue.mock(float.class));
  }
}
