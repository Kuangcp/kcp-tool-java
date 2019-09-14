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
    Integer mock = MockUsuallyValue.mock(100);
    System.out.println(mock);

    Double value = MockUsuallyValue.mock(30.0);
    System.out.println(value);

    String string = MockUsuallyValue.mock("20");
    System.out.println(string);

    Date date = MockUsuallyValue.mock(Date.class);
    System.out.println(date);

    BigDecimal price = MockUsuallyValue.mock(BigDecimal.valueOf(3.4));
    System.out.println(price);
  }

  @Test
  public void testMockByType() {
    System.out.println(MockUsuallyValue.mock(String.class));
    System.out.println(MockUsuallyValue.mock(int.class));
    System.out.println(MockUsuallyValue.mock(double.class));
    System.out.println(MockUsuallyValue.mock(long.class));
    System.out.println(MockUsuallyValue.mock(float.class));
  }
}
