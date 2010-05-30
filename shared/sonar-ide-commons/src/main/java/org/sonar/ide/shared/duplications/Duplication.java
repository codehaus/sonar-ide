package org.sonar.ide.shared.duplications;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public final class Duplication {

  private int lines;
  private int start;
  private int targetStart;
  private String targetResource;

  public Duplication(int lines, int start, int targetStart, String targetResource) {
    this.lines = lines;
    this.start = start;
    this.targetStart = targetStart;
    this.targetResource = targetResource;
  }

  public int getLines() {
    return lines;
  }

  public void setLines(int lines) {
    this.lines = lines;
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getTargetStart() {
    return targetStart;
  }

  public void setTargetStart(int targetStart) {
    this.targetStart = targetStart;
  }

  public String getTargetResource() {
    return targetResource;
  }

  public void setTargetResource(String targetResource) {
    this.targetResource = targetResource;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).
        append("lines", lines).
        append("start", start).
        append("targetStart", targetStart).
        append("targetResource", targetResource).
        toString();
  }
}
