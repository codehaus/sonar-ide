package org.sonar.ide.shared.coverage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Mandrikov
 */
public class CoverageData {

  private Map<Integer, Integer> coverageLineHits = new HashMap<Integer, Integer>();

  protected CoverageData(Map<Integer, String> coverageLineHits) {
    for (Map.Entry<Integer, String> entry : coverageLineHits.entrySet()) {
      this.coverageLineHits.put(entry.getKey(), Integer.parseInt(entry.getValue()));
    }
  }

  public boolean isCovered(int line) {
    return coverageLineHits.containsKey(line) && coverageLineHits.get(line) > 0;
  }

}
