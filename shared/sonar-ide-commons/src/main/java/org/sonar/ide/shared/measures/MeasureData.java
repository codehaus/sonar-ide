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
