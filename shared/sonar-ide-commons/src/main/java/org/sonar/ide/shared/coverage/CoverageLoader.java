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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

/**
 * @author Evgeny Mandrikov
 */
public final class CoverageLoader {
  public static final String COVERAGE_LINE_HITS_DATA_KEY = "coverage_line_hits_data";
  public static final String BRANCH_COVERAGE_HITS_DATA_KEY = "branch_coverage_hits_data";

  public static CoverageData getCoverage(final Sonar sonar, final String resourceKey) {
    return new CoverageData(getCoverageLineHits(sonar, resourceKey), getBranchCoverageHits(sonar, resourceKey));
  }

  public static Collection<CoverageLine> getCoverageLines(final Sonar sonar, final String resourceKey) {
    final Map<Integer, String> hitsByLine = getCoverageLineHits(sonar, resourceKey);
    final Map<Integer, String> branchHitsByLine = getBranchCoverageHits(sonar, resourceKey);
    final Collection<CoverageLine> coverageLines = new ArrayList<CoverageLine>();
    for (final Integer line : hitsByLine.keySet()) {
      coverageLines.add(new CoverageLine(line, hitsByLine.get(line), branchHitsByLine.get(line)));
    }
    return coverageLines;
  }

  public static Map<Integer, String> getCoverageLineHits(final Sonar sonar, final String resourceKey) {
    return getMeasure(sonar, resourceKey, COVERAGE_LINE_HITS_DATA_KEY);
  }

  public static Map<Integer, String> getBranchCoverageHits(final Sonar sonar, final String resourceKey) {
    return getMeasure(sonar, resourceKey, BRANCH_COVERAGE_HITS_DATA_KEY);
  }

  protected static Map<Integer, String> getMeasure(final Sonar sonar, final String resourceKey, final String metricKey) {
    final ResourceQuery query = new ResourceQuery(resourceKey)
    .setMetrics(metricKey);
    System.out.println(query.getUrl());
    final Resource resource = sonar.find(query);
    final Measure measure = resource.getMeasure(metricKey);
    return unmarshall(measure.getData());
  }

  protected static Map<Integer, String> unmarshall(final String data) {
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
  private CoverageLoader() {
  }
}
