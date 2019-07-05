package com.github.kuangcp.time;

import java.time.Duration;

/**
 * @author Myth on 2016年9月29日 下午7:47:35
 */
public class GetRunTime {

  private long startNano;

  public GetRunTime startCount() {
    startNano = System.nanoTime();
    return this;
  }

  public void endCount(String info) {
    long endNano = System.nanoTime();
    long totalNanos = endNano - startNano;
    Duration duration = Duration.ofNanos(endNano - startNano);

    String format = "▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁\n"
        + "▌   Info: %s\n"
        + "▌  Total: %-3sns\n"
        + "▌ Format: %sh %sm %ss %sms %sμs\n";

    System.out.println(String.format(format, info,
        duration.getNano(), duration.toHours(), duration.toMinutes(),
        duration.getSeconds(), duration.toMillis(), totalNanos / 1000));
  }

  public void endCount() {
    long endNano = System.nanoTime();
    long totalNanos = endNano - startNano;
    Duration duration = Duration.ofNanos(endNano - startNano);

    String format = "▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁\n"
        + "▌  Total: %-3sns\n▌ Format: %sh %sm %ss %sms %sμs\n";
    System.out.println(String.format(format, duration.getNano(), duration.toHours(),
        duration.toMinutes(), duration.getSeconds(), duration.toMillis(), totalNanos / 1000));
  }

  public void endCountOneLine(String info) {
    long end = System.nanoTime();
    long totalNanos = end - startNano;
    String format = "Total:%6sns(%sms), Info: %s";
    System.out.println(String.format(format, totalNanos, totalNanos / 1000_000, info));
  }

  public void endCountOneLine() {
    long end = System.nanoTime();
    long totalNanos = end - startNano;
    String format = "Total:%6sns(%sms)";
    System.out.println(String.format(format, totalNanos, totalNanos / 1000_000));
  }

  protected void endWithDebug(String info) {
    long end = System.nanoTime();
    long totalNanos = end - startNano;
    String format = "Total:%6sns, start:%s Info: %s";
    System.out.println(String.format(format, totalNanos, startNano, info));
  }
}
