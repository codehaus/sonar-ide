/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Sonar-IDE is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar-IDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar-IDE; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.shared;

import org.apache.commons.lang.StringUtils;

/**
 * @author Evgeny Mandrikov
 */
public final class SonarUrlUtils {
  public static final String HOST_DEFAULT = "http://localhost:9000";

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
