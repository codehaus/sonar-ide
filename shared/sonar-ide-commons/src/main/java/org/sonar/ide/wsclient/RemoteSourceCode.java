package org.sonar.ide.wsclient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.sonar.ide.api.Logs;
import org.sonar.ide.api.SourceCode;
import org.sonar.ide.api.SourceCodeDiff;
import org.sonar.ide.shared.coverage.CoverageData;
import org.sonar.ide.shared.coverage.CoverageLoader;
import org.sonar.ide.shared.duplications.Duplication;
import org.sonar.ide.shared.duplications.DuplicationUtils;
import org.sonar.ide.shared.measures.MeasureData;
import org.sonar.ide.shared.measures.MeasuresLoader;
import org.sonar.ide.shared.violations.ViolationUtils;
import org.sonar.wsclient.services.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

  public RemoteSourceCode(String key) {
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
  public SourceCode setLocalContent(String content) {
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
    return MeasuresLoader.getMeasures(index.getSonar(), getKey());
  }

  /**
   * {@inheritDoc}
   */
  public CoverageData getCoverage() {
    return CoverageLoader.getCoverage(index.getSonar(), getKey());
  }

  /**
   * {@inheritDoc}
   */
  public List<Violation> getViolations() {
    Logs.INFO.info("Loading violations for {}", getKey());
    Collection<Violation> violations = index.getSonar().findAll(ViolationQuery.createForResource(getKey()));
    Logs.INFO.info("Loaded {} violations: {}", violations.size(), ViolationUtils.toString(violations));
    return ViolationUtils.convertLines(violations, getDiff());
  }

  /**
   * {@inheritDoc}
   */
  public List<Duplication> getDuplications() {
    Logs.INFO.info("Loading duplications for {}", getKey());
    Resource resource = index.getSonar().find(ResourceQuery.createForMetrics(getKey(), DuplicationUtils.DUPLICATIONS_DATA));
    Measure measure = resource.getMeasure(DuplicationUtils.DUPLICATIONS_DATA);
    if (measure == null) {
      return Collections.emptyList();
    }
    List<Duplication> duplications = DuplicationUtils.parse(measure.getData());
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
  public int compareTo(SourceCode resource) {
    return key.compareTo(resource.getKey());
  }

  @Override
  public boolean equals(Object obj) {
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

  protected RemoteSourceCode setRemoteSonarIndex(RemoteSonarIndex index) {
    this.index = index;
    return this;
  }
}
