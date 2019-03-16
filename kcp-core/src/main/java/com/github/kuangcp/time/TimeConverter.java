package com.github.kuangcp.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author kuangcp on 2/14/19-3:10 PM
 */
public class TimeConverter {

  public long toMills(LocalDateTime target) {
    return toMills(target, ZoneOffset.systemDefault());
  }

  public long toMills(LocalDateTime target, ZoneId zone) {
    Instant instant = target.atZone(zone).toInstant();

    return instant.toEpochMilli();
  }
}
