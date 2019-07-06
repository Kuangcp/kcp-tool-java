package com.github.kuangcp.time;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@Ignore
public class GetRunTimeTest {

  private GetRunTime getRunTime = new GetRunTime();

  @Test
  public void testStartCount() {
    getRunTime.startCount();
    int a = 99999999;
    for (int i = 0; i < 100; i++) {
      a = a ^ i;
      MD5(a + "");
    }
    getRunTime.endCountOneLine("第一个");

    getRunTime.startCount();
    for (int i = 0; i < 10000; i++) {
      a = a ^ i;
      MD5(a + "");
    }
    getRunTime.endCountOneLine("第二个");
  }

  /**
   * Java 内置md5加密
   */
  private static Optional<String> MD5(String s) {
    char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F'};

    try {
      byte[] btInput = s.getBytes();
      // 获得MD5摘要算法的 MessageDigest 对象
      MessageDigest mdInst = MessageDigest.getInstance("MD5");
      // 使用指定的字节更新摘要
      mdInst.update(btInput);
      // 获得密文
      byte[] md = mdInst.digest();
      // 把密文转换成十六进制的字符串形式
      int j = md.length;
      char[] str = new char[j * 2];
      int k = 0;
      for (byte byte0 : md) {
        str[k++] = hexDigits[byte0 >>> 4 & 0xf];
        str[k++] = hexDigits[byte0 & 0xf];
      }
      return Optional.of(Arrays.toString(str));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  @Test
  public void testSimple() {
    getRunTime.startCount();
    getRunTime.endCountOneLine("end count");
    getRunTime.endCount();
  }

  @Test
  public void testMultiple() {
    for (int i = 0; i < 10; i++) {
      int finalI = i;
      new Thread(() -> {
        new GetRunTime().startCount().endWithDebug(finalI + "");
        try {
          TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
    }
  }
}