package com.github.kuangcp.properties;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuangcp on 2019-08-06 上午12:59
 */
@Slf4j
public class ReadProperties {

  private Properties conf = new Properties();

  /**
   * @param filePath 例如 resources 目录下 a.properties
   */
  public ReadProperties(String filePath) {
    try {
      URL resource = this.getClass().getClassLoader().getResource(filePath);
      if (Objects.isNull(resource)) {
        throw new RuntimeException("filePath not exist");
      }

      File file = new File(resource.getPath());
      conf.load(new FileInputStream(file));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  public Optional<String> getString(String key) {
    return Optional.ofNullable(conf.getProperty(key));
  }

  public Optional<String> getStringWithUTF8(String key) {
    return getString(key)
        .map(v -> new String(v.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
  }

  public Optional<Integer> getInt(String key) {
    return getString(key).map(Integer::parseInt);
  }

  public Optional<Double> getDouble(String key) {
    return getString(key).map(Double::parseDouble);
  }
}
