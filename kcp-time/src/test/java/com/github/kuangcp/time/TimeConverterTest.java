package com.github.kuangcp.time;

import java.time.LocalDateTime;
import java.time.Month;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author kuangcp on 2/14/19-3:14 PM
 */
public class TimeConverterTest {

  private TimeConverter timeConverter = new TimeConverter();

  @Test
  public void testToMills() {
    long result = timeConverter.toMills(LocalDateTime.of(2019, Month.FEBRUARY, 14, 15, 14, 45));
    Assert.assertEquals(1550128485000L, result);
  }
}
