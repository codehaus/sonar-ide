package org.sonar.ide.shared;

import org.apache.commons.lang.StringUtils;

/**
 * @author Evgeny Mandrikov
 */
public final class SonarUrlUtils {
  private static final String PROJECT = "/project/index/";
  private static final String COMPONENTS = "/components/index/";
  private static final String RESOURCE = "/resource/index/";
  private static final String TIMEMACHINE = "/timemachine/index/";
  private static final String MEASURES_DRILLDOWN = "/drilldown/measures/";
  private static final String VIOLATIONS_DRILLDOWN = "/drilldown/violations/";

  public static String getDashboard(String host, String resourceKey) {
    return getUrlBuilder(host, PROJECT, resourceKey).toString();
  }

  public static String getComponents(String host, String resourceKey) {
    return getUrlBuilder(host, COMPONENTS, resourceKey).toString();
  }

  public static String getResource(String host, String resourceKey) {
    return getUrlBuilder(host, RESOURCE, resourceKey).toString();
  }

  public static String getTimemachine(String host, String resourceKey) {
    return getUrlBuilder(host, TIMEMACHINE, resourceKey).toString();
  }

  public static String getMeasuresDrilldown(String host, String resourceKey, String metric) {
    StringBuilder url = getUrlBuilder(host, MEASURES_DRILLDOWN, resourceKey);
    append(url, "metric", metric);
    return url.toString();
  }

  public static String getMeasuresDrilldown(String host, String resourceKey) {
    return getMeasuresDrilldown(host, resourceKey, null);
  }

  public static String getViolationsDrilldown(String host, String resourceKey) {
    return getUrlBuilder(host, VIOLATIONS_DRILLDOWN, resourceKey).toString();
  }

  private static StringBuilder getUrlBuilder(String host, String prefix, String resourceKey) {
    StringBuilder sb = new StringBuilder()
        .append(host)
        .append(prefix);
    if (StringUtils.isNotBlank(resourceKey)) {
      sb.append(resourceKey);
    }
    sb.append("?");
    return sb;
  }

  private static void append(StringBuilder url, String paramKey, String paramValue) {
    if (StringUtils.isNotBlank(paramValue)) {
      url.append(paramKey)
          .append('=')
          .append(paramValue)
          .append('&');
    }
  }

  /**
   * Hide utility-class constructor.
   */
  private SonarUrlUtils() {
  }
}
