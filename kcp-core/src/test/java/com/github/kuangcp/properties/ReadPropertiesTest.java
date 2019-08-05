package com.github.kuangcp.properties;

import java.util.Optional;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author kuangcp on 2019-08-06 上午1:00
 */
public class ReadPropertiesTest {

  @Test
  public void testRead() {
    ReadProperties read = new ReadProperties("read.properties");
    Optional<String> nameOpt = read.getString("name");
    assertThat(nameOpt.isPresent(), equalTo(true));
    System.out.println(nameOpt.get());

    //配置文件含中文需要转码
    nameOpt = read.getStringWithUTF8("name");
    assertThat(nameOpt.isPresent(), equalTo(true));
    System.out.println(nameOpt.get());
  }
}
