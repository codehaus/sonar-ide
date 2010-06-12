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

package org.sonar.ide.shared.coverage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public final class CoverageData {
  private Map<Integer, String> hitsByLine = new HashMap<Integer, String>();
  private Map<Integer, String> branchHitsByLine = new HashMap<Integer, String>();

  protected CoverageData(Map<Integer, String> hitsByLine, Map<Integer, String> branchHitsByLine) {
    this.hitsByLine = hitsByLine;
    this.branchHitsByLine = branchHitsByLine;
  }

  public String getHitsByLine(int line) {
    return hitsByLine.get(line);
  }

  public String getBranchHitsByLine(int line) {
    return branchHitsByLine.get(line);
  }

  public CoverageStatus getCoverageStatus(int line) {
    String hits = hitsByLine.get(line);
    String branchHits = branchHitsByLine.get(line);
    boolean hasLineCoverage = (null != hits);
    boolean hasBranchCoverage = (null != branchHits);
    boolean lineIsCovered = (hasLineCoverage && Integer.parseInt(hits) > 0);
    boolean branchIsCovered = (hasBranchCoverage && "100%".equals(branchHits));

    if (lineIsCovered) {
      if (branchIsCovered) {
        return CoverageStatus.FULLY_COVERED;
      } else if (hasBranchCoverage) {
        return CoverageStatus.PARTIALLY_COVERED;
      } else {
        return CoverageStatus.FULLY_COVERED;
      }
    } else if (hasLineCoverage) {
      return CoverageStatus.UNCOVERED;
    }
    return CoverageStatus.NO_DATA;
  }

  public enum CoverageStatus {
    NO_DATA,
    FULLY_COVERED,
    PARTIALLY_COVERED,
    UNCOVERED
  }

}
