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

package org.sonar.ide.shared;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class SonarUrlUtilsTest {
  private static final String HOST = "http://localhost";
  private static final String KEY = "test:test";

  @Test
  public void testGetDashboard() throws Exception {
    assertThat(SonarUrlUtils.getDashboard(HOST, null), is("http://localhost/project/index/?"));
    assertThat(SonarUrlUtils.getDashboard(HOST, KEY), is("http://localhost/project/index/test:test?"));
  }

  @Test
  public void testGetComponents() throws Exception {
    assertThat(SonarUrlUtils.getComponents(HOST, null), is("http://localhost/components/index/?"));
    assertThat(SonarUrlUtils.getComponents(HOST, KEY), is("http://localhost/components/index/test:test?"));
  }

  @Test
  public void testGetTimemachine() throws Exception {
    assertThat(SonarUrlUtils.getTimemachine(HOST, null), is("http://localhost/timemachine/index/?"));
    assertThat(SonarUrlUtils.getTimemachine(HOST, KEY), is("http://localhost/timemachine/index/test:test?"));
  }

  @Test
  public void testGetResource() throws Exception {
    assertThat(SonarUrlUtils.getResource(HOST, null), is("http://localhost/resource/index/?"));
    assertThat(SonarUrlUtils.getResource(HOST, KEY), is("http://localhost/resource/index/test:test?"));
  }

  @Test
  public void testGetMeasuresDrilldown() throws Exception {
    assertThat(SonarUrlUtils.getMeasuresDrilldown(HOST, null), is("http://localhost/drilldown/measures/?"));
    assertThat(SonarUrlUtils.getMeasuresDrilldown(HOST, KEY), is("http://localhost/drilldown/measures/test:test?"));

    assertThat(SonarUrlUtils.getMeasuresDrilldown(HOST, KEY, null), is("http://localhost/drilldown/measures/test:test?"));
    assertThat(SonarUrlUtils.getMeasuresDrilldown(HOST, KEY, "classes"), is("http://localhost/drilldown/measures/test:test?metric=classes&"));
  }

  @Test
  public void testGetViolationsDrilldown() throws Exception {
    assertThat(SonarUrlUtils.getViolationsDrilldown(HOST, null), is("http://localhost/drilldown/violations/?"));
    assertThat(SonarUrlUtils.getViolationsDrilldown(HOST, KEY), is("http://localhost/drilldown/violations/test:test?"));
  }
}
