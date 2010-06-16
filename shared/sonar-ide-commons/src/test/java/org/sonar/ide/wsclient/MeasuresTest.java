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

package org.sonar.ide.wsclient;

import org.junit.Ignore;
import org.junit.Test;
import org.sonar.ide.shared.measures.MeasureData;
import org.sonar.ide.test.SonarIdeTestCase;
import org.sonar.ide.wsclient.RemoteSonar;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class MeasuresTest extends SonarIdeTestCase {

  @Test
  public void testGetMeasures() throws Exception {
    List<MeasureData> measures = getMeasures(getProject("measures"), "Measures");

    assertThat(measures.size(), greaterThan(0));
  }

  @Test
  @Ignore("Not ready")
  @SuppressWarnings("unchecked")
  public void notLoadDataMeasures() throws Exception {
    List measures = getMeasures(getProject("measures"), "Measures");

    assertThat((List<Object>) measures, not(hasItem(hasProperty("name", is("Coverage hits data")))));
  }

  private List<MeasureData> getMeasures(File project, String className) throws Exception {
    return new RemoteSonar(getTestServer().getSonar())
        .search(getProjectKey(project) + ":[default]." + className)
        .getMeasures();
  }
}
