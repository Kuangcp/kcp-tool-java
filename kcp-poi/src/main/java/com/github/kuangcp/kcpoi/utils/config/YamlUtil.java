package com.github.kuangcp.kcpoi.utils.config;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp on 18-2-27  下午3:37 Yaml的读写工具类
 *
 * @author kuangcp
 */
@Slf4j
public class YamlUtil {

  private static YAMLFactory factory = new YAMLFactory();
  private static ObjectMapper mapper = new ObjectMapper(factory);

  /**
   * @param object 对象  对象的定义切记要有set get, 而且不能重载这些方法, 不然就会稀奇古怪的错误
   * @param filePath 绝对路径,目前存在问题
   * @return true 创建成功
   */
  public static boolean createFile(Object object, String filePath) {
    log.info("path={}", filePath);
    factory.setCodec(mapper);
    YAMLGenerator generator;
    try {
      generator = factory.createGenerator(new FileOutputStream(filePath), JsonEncoding.UTF8);
      generator.useDefaultPrettyPrinter();
      generator.writeObject(object);
      return true;
    } catch (IOException e) {
      log.error("create file error", e);
      return false;
    }
  }


  /**
   * 读取yml文件
   *
   * @param target 配置文件对应的类
   * @param filePath 配置文件路径
   * @return 对象, 找不到则是null
   */
  public static <T> T readFile(Class<T> target, String filePath) {
    ObjectMapper mapper = new ObjectMapper(factory);
    try {
      return mapper.readValue(new File(filePath), target);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("read file error", e);
      return null;
    }
  }


  /**
   * 读取yml文件
   *
   * @param target 配置文件对应的类
   * @return 对象, 找不到则是null
   */
  public static <T> Optional<T> readFile(Class<T> target, InputStream inputStream) {
    if (Objects.isNull(inputStream)) {
      return Optional.empty();
    }
    ObjectMapper mapper = new ObjectMapper(factory);
    try {
      return Optional.ofNullable(mapper.readValue(inputStream, target));
    } catch (Exception e) {
      e.printStackTrace();
      log.error("read file error", e);
      return Optional.empty();
    }
  }
}