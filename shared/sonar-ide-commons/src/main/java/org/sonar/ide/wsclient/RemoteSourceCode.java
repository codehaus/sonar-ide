package org.sonar.ide.wsclient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.sonar.ide.api.SourceCode;
import org.sonar.ide.shared.coverage.CoverageData;
import org.sonar.ide.shared.coverage.CoverageLoader;
import org.sonar.ide.shared.duplications.Duplication;
import org.sonar.ide.shared.duplications.DuplicationsLoader;
import org.sonar.ide.shared.measures.MeasureData;
import org.sonar.ide.shared.measures.MeasuresLoader;
import org.sonar.ide.shared.violations.ViolationsLoader;
import org.sonar.wsclient.services.Source;
import org.sonar.wsclient.services.SourceQuery;
import org.sonar.wsclient.services.Violation;

import java.util.List;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
class RemoteSourceCode implements SourceCode {
  private final String key;
  private RemoteSonarIndex index;
  private String content;

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
    this.content = content;
    return this;
  }

  private String getLocalContent() {
    if (content == null) {
      return "";
    }
    return content;
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
    return ViolationsLoader.getViolations(index.getSonar(), getKey(), getLocalContent());
  }

  /**
   * {@inheritDoc}
   */
  public List<Duplication> getDuplications() {
    return DuplicationsLoader.getDuplications(index.getSonar(), getKey(), getLocalContent());
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
