package org.sonar.ide.shared.coverage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Mandrikov
 */
public final class CoverageUtils {
  public static final String COVERAGE_LINE_HITS_DATA_KEY = "coverage_line_hits_data";
  public static final String BRANCH_COVERAGE_HITS_DATA_KEY = "branch_coverage_hits_data";

  public static Map<Integer, String> unmarshall(final String data) {
    final Map<Integer, String> result = new HashMap<Integer, String>();
    final String[] values = data.split(";");
    for (final String value : values) {
      final String[] pair = value.split("=");
      final int line = Integer.parseInt(pair[0]);
      result.put(line, pair[1]);
    }
    return result;
  }

  /**
   * Hide utility-class constructor.
   */
  private CoverageUtils() {
  }


}
