package org.sonar.ide.shared.coverage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Mandrikov
 */
public final class CoverageData {
  private Map<Integer, String> hitsByLine = new HashMap<Integer, String>();
  private Map<Integer, String> branchHitsByLine = new HashMap<Integer, String>();

  protected CoverageData(Map<Integer, String> hitsByLine, Map<Integer, String> branchHitsByLine) {
    this.hitsByLine = hitsByLine;
    this.branchHitsByLine = branchHitsByLine;
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
