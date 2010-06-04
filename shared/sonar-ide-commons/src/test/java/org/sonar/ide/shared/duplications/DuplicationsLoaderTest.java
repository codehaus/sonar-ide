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

package org.sonar.ide.shared.duplications;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.ide.test.AbstractSonarIdeTest;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class DuplicationsLoaderTest extends AbstractSonarIdeTest {
  @BeforeClass
  public static void init() throws Exception {
    AbstractSonarIdeTest.init();
  }

  @AfterClass
  public static void cleanup() throws Exception {
    AbstractSonarIdeTest.cleanup();
  }

  @Test
  public void testGetDuplications() throws Exception {
    List<Duplication> duplications = getDuplications(getProject("duplications"), "Duplications");
    assertThat(duplications.size(), is(2));
    Duplication duplication = duplications.get(0);
    assertThat(duplication.getLines(), greaterThan(0));
    assertThat(duplication.getStart(), greaterThan(0));
    assertThat(duplication.getTargetStart(), greaterThan(0));
    assertThat(duplication.getTargetResource(), allOf(notNullValue(), not("")));
  }

  private List<Duplication> getDuplications(File project, String className) throws Exception {
    return DuplicationsLoader.getDuplications(
        getTestServer().getSonar(),
        getProjectKey(project) + ":[default]." + className,
        FileUtils.readFileToString(getProjectFile(project, "/src/main/java/" + className + ".java"))
    );
  }
}
