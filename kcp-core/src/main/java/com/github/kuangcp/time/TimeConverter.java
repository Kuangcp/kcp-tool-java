package com.github.kuangcp.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

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

  public Optional<Date> toDate(LocalDateTime dateTime) {
    if (Objects.isNull(dateTime)) {
      return Optional.empty();
    }
    return Optional.of(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
  }

  public Optional<LocalDateTime> toLocalDateTime(Date date) {
    if (Objects.isNull(date)) {
      return Optional.empty();
    }
    return Optional.of(LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.systemDefault()));
  }
}
