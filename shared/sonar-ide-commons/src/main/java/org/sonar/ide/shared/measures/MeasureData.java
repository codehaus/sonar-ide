package org.sonar.ide.shared.measures;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public final class MeasureData {

  private String name;
  private String domain;
  private String value;

  public String getName() {
    return name;
  }

  public MeasureData setName(String name) {
    this.name = name;
    return this;
  }

  public String getDomain() {
    return domain;
  }

  public MeasureData setDomain(String domain) {
    this.domain = domain;
    return this;
  }

  public String getValue() {
    return value;
  }

  public MeasureData setValue(String value) {
    this.value = value;
    return this;
  }

}
