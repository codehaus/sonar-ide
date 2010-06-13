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

package org.sonar.ide.shared.measures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.MetricQuery;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public final class MeasuresLoader {
  /**
   * Returns measures from specified sonar for specified resource.
   *
   * @param sonar       sonar
   * @param resourceKey resource key
   * @return measures
   */
  public static List<MeasureData> getMeasures(Sonar sonar, String resourceKey) {
    // TODO Godin: This is not optimal. Would be better to load metrics only once.
    List<Metric> metrics = sonar.findAll(MetricQuery.all());
    Map<String, Metric> metricsByKey = new HashMap<String, Metric>();
    // TODO Godin: We shouldn't load all measures.
    for (Metric metric : metrics) {
      metricsByKey.put(metric.getKey(), metric);
    }
    String[] metricKeys = metricsByKey.keySet().toArray(new String[metrics.size()]);
    ResourceQuery query = ResourceQuery.createForMetrics(resourceKey, metricKeys);
    Resource resource = sonar.find(query);
    List<MeasureData> result = new ArrayList<MeasureData>();
    for (Measure measure : resource.getMeasures()) {
      Metric metric = metricsByKey.get(measure.getMetricKey());
      result.add(new MeasureData()
          .setName(metric.getName())
          .setDomain(metric.getDomain())
          .setValue(measure.getFormattedValue())
      );
    }
    return result;
  }

  /**
   * Hide utility-class constructor.
   */
  private MeasuresLoader() {
  }
}
