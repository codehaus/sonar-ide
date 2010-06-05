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

package org.sonar.ide.ui;

import org.sonar.ide.shared.MetricUtils;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.MetricQuery;
import org.sonar.wsclient.services.Resource;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Mandrikov
 * @deprecated since 0.2, instead of it use {@link org.sonar.ide.ui.MeasuresPanel}
 */
@Deprecated
public class MeasuresViewer extends JTabbedPane {
  private final String resourceKey;

  public MeasuresViewer(Sonar sonar, AbstractIconLoader iconLoader, String resourceKey) {
    this.resourceKey = resourceKey;
    MetricQuery query = MetricQuery.all();
    Map<String, List<Metric>> domains = MetricUtils.splitByDomain(sonar.findAll(query));
    for (Map.Entry<String, List<Metric>> entry : domains.entrySet()) {
      addTab(entry.getKey(), getTab(sonar, iconLoader, entry.getValue()));
    }
  }

  private JPanel getTab(Sonar sonar, AbstractIconLoader iconLoader, List<Metric> metrics) {
    String[] keys = new String[metrics.size()];
    int i = 0;
    for (Metric metric : metrics) {
      keys[i] = metric.getKey();
      i++;
    }
    return new DomainViewer(sonar, iconLoader, resourceKey, keys);
  }

  static class DomainViewer extends AbstractViewer {
    protected DomainViewer(Sonar sonar, AbstractIconLoader iconLoader, String resourceKey, String... metrics) {
      super(sonar, iconLoader, resourceKey, metrics);
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    public String getTitle() {
      return "TODO";
    }

    @Override
    protected void display(Resource resource) {
      for (String metric : metrics) {
        Measure measure = resource.getMeasure(metric);
        addCell(measure);
      }
    }
  }
}
