/*
 * Copyright (C) 2010 Evgeny Mandrikov, Jérémie Lagarde
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
package org.sonar.ide.shared.coverage;

/**
 * @author Jérémie Lagarde
 * @since 0.2
 */
public class CoverageLine {

  private final Integer line;
  private final String hits;
  private final String branchHits;

  protected CoverageLine(final Integer line, final String hits, final String branchHits) {
    super();
    this.line = line;
    this.hits = hits;
    this.branchHits = branchHits;
  }

  public Integer getLine() {
    return line;
  }

  public String getHits() {
    return hits;
  }

  public String getBranchHits() {
    return branchHits;
  }
}
