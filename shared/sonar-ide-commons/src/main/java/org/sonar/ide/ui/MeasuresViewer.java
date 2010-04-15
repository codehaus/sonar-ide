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
 */
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
