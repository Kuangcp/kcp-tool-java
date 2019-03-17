package com.github.kuangcp.time;

import java.time.Instant;

/**
 * @author Myth on 2016年9月29日 下午7:47:35
 */
public class GetRunTime {

  private long start;

  public GetRunTime startCount() {
    start = System.currentTimeMillis();
    return this;
  }

  public void endCount(String info) {
    long end = System.currentTimeMillis();
    long totalMillis = end - start;
    long ms = totalMillis, sec, min, hour;

    hour = ms / 3600_000;
    ms -= hour * 3600_000;
    min = ms / 60_000;
    ms -= min * 60_000;
    sec = ms / 1_000;
    ms -= sec * 1_000;

    String format = "▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁\n▌   Info: %s\n"
        + "▌  Total: %-3sms\n▌ Format: %sh %sm %ss %sms\n";
    System.out.println(String.format(format, info, totalMillis, hour, min, sec, ms));
  }

  public void endCount() {
    long end = System.currentTimeMillis();
    long totalMillis = end - start;
    long ms = totalMillis, sec, min, hour;

    hour = ms / 3600_000;
    ms -= hour * 3600_000;
    min = ms / 60_000;
    ms -= min * 60_000;
    sec = ms / 1_000;
    ms -= sec * 1_000;

    String format = "▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁\n"
        + "▌  Total: %-3sms\n▌ Format: %sh %sm %ss %sms\n";
    System.out.println(String.format(format, totalMillis, hour, min, sec, ms));
  }

  public void endCountOneLine(String info) {
    long end = Instant.now().toEpochMilli();
    long totalMillis = end - start;
    String format = "Total:%6sms, Info: %s";
    System.out.println(String.format(format, totalMillis, info));
  }

  public void endCountOneLine() {
    long end = Instant.now().toEpochMilli();
    long totalMillis = end - start;
    String format = "Total:%6sms";
    System.out.println(String.format(format, totalMillis));
  }

  protected void endWithDebug(String info) {
    long end = Instant.now().toEpochMilli();
    long totalMillis = end - start;
    String format = "Total:%6sms, %s Info: %s";
    System.out.println(String.format(format, totalMillis, start, info));
  }
}
