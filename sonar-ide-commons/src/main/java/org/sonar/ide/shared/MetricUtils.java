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
