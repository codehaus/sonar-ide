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

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.MetricQuery;

import java.util.List;

/**
 * @author Jérémie Lagarde
 * @deprecated since 0.2
 */
@Deprecated
public class MetricsLoader {

  /**
   * Returns metrics from specified sonar server.
   *
   * @param sonar     sonar
   * @param metricKey metric key
   * @return metrics
   */
  public static List<Metric> getMetrics(final Sonar sonar, final String metricKey) {
    final MetricQuery metricQuery = MetricQuery.byKey(metricKey);
    return sonar.findAll(metricQuery);
  }

  /**
   * Hide utility-class constructor.
   */
  private MetricsLoader() {
  }
}
