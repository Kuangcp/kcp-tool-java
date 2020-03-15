package com.github.kuangcp.kcpoi.utils.config;

import com.github.kuangcp.kcpoi.config.ConfigManager;
import com.github.kuangcp.kcpoi.config.MainConfig;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Created by https://github.com/kuangcp on 18-3-3  下午7:23
 *
 * @author kuangcp
 */
public class DateUtil {

  private static MainConfig mainConfig = ConfigManager.getInstance();

  public static Date parse(String date) throws ParseException {
    if (!ConfigManager.checkConfig(mainConfig)) {
      return null;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat(mainConfig.getDateFormat());
    if (Objects.isNull(date) || date.isEmpty()) {
      return null;
    }
    return dateFormat.parse(date);
  }
}
