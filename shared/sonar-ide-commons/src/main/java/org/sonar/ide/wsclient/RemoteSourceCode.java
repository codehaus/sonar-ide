package org.sonar.ide.wsclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.sonar.ide.api.Logs;
import org.sonar.ide.api.SourceCode;
import org.sonar.ide.api.SourceCodeDiff;
import org.sonar.ide.shared.coverage.CoverageData;
import org.sonar.ide.shared.coverage.CoverageUtils;
import org.sonar.ide.shared.duplications.Duplication;
import org.sonar.ide.shared.duplications.DuplicationUtils;
import org.sonar.ide.shared.measures.MeasureData;
import org.sonar.ide.shared.violations.ViolationUtils;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.MetricQuery;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;
import org.sonar.wsclient.services.Source;
import org.sonar.wsclient.services.SourceQuery;
import org.sonar.wsclient.services.Violation;
import org.sonar.wsclient.services.ViolationQuery;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
class RemoteSourceCode implements SourceCode {
  private final String key;
  private RemoteSonarIndex index;

  private String localContent;

  /**
   * Lazy initialization - see {@link #getDiff()}.
   */
  private SourceCodeDiff diff;

  /**
   * Lazy initialization - see {@link #getRemoteContent()}.
   */
  private String[] remoteContent;

  public RemoteSourceCode(final String key) {
    this.key = key;
  }

  /**
   * {@inheritDoc}
   */
  public String getKey() {
    return key;
  }

  /**
   * {@inheritDoc}
   */
  public SourceCode setLocalContent(final String content) {
    this.localContent = content;
    return this;
  }

  private String getLocalContent() {
    if (localContent == null) {
      return "";
    }
    return localContent;
  }

  private String[] getRemoteContent() {
    if (remoteContent == null) {
      remoteContent = SimpleSourceCodeDiffEngine.getLines(getCode());
    }
    return remoteContent;
  }

  private SourceCodeDiff getDiff() {
    if (diff == null) {
      diff = index.getDiffEngine().diff(SimpleSourceCodeDiffEngine.split(getLocalContent()), getRemoteContent());
    }
    return diff;
  }

  /**
   * {@inheritDoc}
   */
  public List<MeasureData> getMeasures() {
    // TODO Godin: This is not optimal. Would be better to load metrics only once.
    final List<Metric> metrics = index.getSonar().findAll(MetricQuery.all());
    final Map<String, Metric> metricsByKey = new HashMap<String, Metric>();
    // TODO Godin: We shouldn't load all measures.
    for (final Metric metric : metrics) {
      metricsByKey.put(metric.getKey(), metric);
    }
    final String[] metricKeys = metricsByKey.keySet().toArray(new String[metrics.size()]);
    final ResourceQuery query = ResourceQuery.createForMetrics(getKey(), metricKeys);
    final Resource resource = index.getSonar().find(query);
    final List<MeasureData> result = new ArrayList<MeasureData>();
    for (final Measure measure : resource.getMeasures()) {
      final Metric metric = metricsByKey.get(measure.getMetricKey());
      result.add(new MeasureData()
      .setName(metric.getName())
      .setDomain(metric.getDomain())
      .setValue(measure.getFormattedValue())
      );
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public CoverageData getCoverage() {
    final Resource resource = index.getSonar().find(ResourceQuery.createForMetrics(
        getKey(),
        CoverageUtils.COVERAGE_LINE_HITS_DATA_KEY, CoverageUtils.BRANCH_COVERAGE_HITS_DATA_KEY
    ));
    final Measure measure = resource.getMeasure(CoverageUtils.COVERAGE_LINE_HITS_DATA_KEY);
    final Measure measure2 = resource.getMeasure(CoverageUtils.BRANCH_COVERAGE_HITS_DATA_KEY);
    if (measure2 != null) {
      return new CoverageData(
          CoverageUtils.unmarshall(measure.getData()),
          CoverageUtils.unmarshall(measure2.getData())
      );
    } else {
      return new CoverageData(
          CoverageUtils.unmarshall(measure.getData()),
          new HashMap<Integer,String>()
      );
    }
  }

  /**
   * {@inheritDoc}
   */
  public List<Violation> getViolations() {
    Logs.INFO.info("Loading violations for {}", getKey());
    final Collection<Violation> violations = index.getSonar().findAll(ViolationQuery.createForResource(getKey()));
    Logs.INFO.info("Loaded {} violations: {}", violations.size(), ViolationUtils.toString(violations));
    return ViolationUtils.convertLines(violations, getDiff());
  }

  /**
   * {@inheritDoc}
   */
  public List<Duplication> getDuplications() {
    Logs.INFO.info("Loading duplications for {}", getKey());
    final Resource resource = index.getSonar().find(ResourceQuery.createForMetrics(getKey(), DuplicationUtils.DUPLICATIONS_DATA));
    final Measure measure = resource.getMeasure(DuplicationUtils.DUPLICATIONS_DATA);
    if (measure == null) {
      return Collections.emptyList();
    }
    final List<Duplication> duplications = DuplicationUtils.parse(measure.getData());
    Logs.INFO.info("Loaded {} duplications: {}", duplications.size(), duplications);
    return DuplicationUtils.convertLines(duplications, getDiff());
  }

  /**
   * {@inheritDoc}
   */
  public Source getCode() {
    return index.getSonar().find(new SourceQuery(getKey()));
  }

  /**
   * {@inheritDoc}
   */
  public int compareTo(final SourceCode resource) {
    return key.compareTo(resource.getKey());
  }

  @Override
  public boolean equals(final Object obj) {
    return (obj instanceof RemoteSourceCode) && (key.equals(((RemoteSourceCode) obj).key));
  }

  @Override
  public int hashCode() {
    return key.hashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).
    append("key", key).
    toString();
  }

  protected RemoteSourceCode setRemoteSonarIndex(final RemoteSonarIndex index) {
    this.index = index;
    return this;
  }
}
