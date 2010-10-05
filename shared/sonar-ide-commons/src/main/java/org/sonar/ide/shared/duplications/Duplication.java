/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.shared.duplications;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.sonar.wsclient.services.Model;

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
