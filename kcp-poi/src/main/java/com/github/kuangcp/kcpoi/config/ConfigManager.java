package com.github.kuangcp.kcpoi.config;

import com.github.kuangcp.kcpoi.utils.config.YamlUtil;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-12-12 08:06
 */
@Slf4j
public class ConfigManager {

  public static final String EXCEL_CONFIG = "excel-config.yml";

  private static MainConfig mainConfig = null;

  private ConfigManager() {

  }

  /**
   * 获取配置实例, 如果有配置文件就覆盖这里默认的配置, 否则采用默认
   *
   * @return 返回配置对象
   */
  public synchronized static MainConfig getInstance() throws RuntimeException {
    if (mainConfig == null) {
      ClassLoader classLoader = MainConfig.class.getClassLoader();
      InputStream inputStream = classLoader.getResourceAsStream(EXCEL_CONFIG);
      Optional<MainConfig> mainConfigOpt = YamlUtil.readFile(MainConfig.class, inputStream);
      MainConfig config = mainConfigOpt.orElseGet(MainConfig::new);
      if (checkConfig(config)) {
        mainConfig = config;
      }
    }
    return mainConfig;
  }

  public static boolean checkConfig(MainConfig config) {
    if (Objects.isNull(config)) {
      return false;
    }
    if (config.getContentStartNum() <= config.getTitleLastRowNum()) {
      log.error("contentStartNum must more than titleLastRowNum");
      return false;
    }
    if (config.getTitleLastRowNum() > 0
        && config.getTitleLastRowNum() < config.getStartRowNum() + 1) {
      log.error("titleLastRowNum must more than startRowNum of 2");
      return false;
    }
    return true;
  }
}
