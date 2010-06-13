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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Test;
import org.sonar.ide.test.SonarIdeTestCase;

/**
 * @author Evgeny Mandrikov
 */
public class CoverageLoaderTest extends SonarIdeTestCase {

  @Test
  public void testGetCoverage() throws Exception {
    CoverageData coverage = getCoverage(getProject("coverage"), "Coverage");
    assertThat(coverage, notNullValue());
    assertThat(coverage.getCoverageStatus(1), is(CoverageData.CoverageStatus.NO_DATA)); // class
    assertThat(coverage.getCoverageStatus(2), is(CoverageData.CoverageStatus.FULLY_COVERED)); // method
    assertThat(coverage.getCoverageStatus(3), is(CoverageData.CoverageStatus.PARTIALLY_COVERED)); // if (false) {
    assertThat(coverage.getCoverageStatus(4), is(CoverageData.CoverageStatus.UNCOVERED)); // System.out.println("Never");
    assertThat(coverage.getCoverageStatus(5), is(CoverageData.CoverageStatus.NO_DATA)); // } else {
    assertThat(coverage.getCoverageStatus(6), is(CoverageData.CoverageStatus.FULLY_COVERED)); // System.out.println("Once");
    assertThat(coverage.getCoverageStatus(7), is(CoverageData.CoverageStatus.NO_DATA)); // }
  }

  private CoverageData getCoverage(File project, String className) throws Exception {
    return CoverageLoader.getCoverage(getTestServer().getSonar(), getProjectKey(project) + ":[default]." + className);
  }
}
