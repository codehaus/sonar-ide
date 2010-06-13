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

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.sonar.ide.test.SonarIdeTestCase;

/**
 * @author Evgeny Mandrikov
 */
public class MeasuresLoaderTest extends SonarIdeTestCase {

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
    return MeasuresLoader.getMeasures(getTestServer().getSonar(), getProjectKey(project) + ":[default]." + className);
  }
}
