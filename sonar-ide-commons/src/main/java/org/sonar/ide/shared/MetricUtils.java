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

import org.sonar.wsclient.services.Metric;

import java.util.*;

/**
 * @author Evgeny Mandrikov
 */
public final class MetricUtils {
  public static Map<String, List<Metric>> splitByDomain(Collection<Metric> metrics) {
    Map<String, List<Metric>> result = new HashMap<String, List<Metric>>();
    for (Metric metric : metrics) {
      String domain = metric.getDomain();
      List<Metric> domainMetrics;
      if (result.containsKey(domain)) {
        domainMetrics = result.get(domain);
      } else {
        domainMetrics = new ArrayList<Metric>();
        result.put(domain, domainMetrics);
      }
      domainMetrics.add(metric);
    }
    return result;
  }

  /**
   * Hide utility-class constructor.
   */
  private MetricUtils() {
  }
}
