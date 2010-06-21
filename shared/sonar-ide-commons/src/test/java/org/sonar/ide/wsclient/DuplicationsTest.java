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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.sonar.ide.shared.duplications.Duplication;

/**
 * @author Evgeny Mandrikov
 */
public class DuplicationsTest extends AbstractRemoteTestCase {

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
    return getRemoteSonar().search(getProjectKey(project) + ":[default]." + className).setLocalContent(
        FileUtils.readFileToString(getProjectFile(project, "/src/main/java/" + className + ".java"))).getDuplications();
  }
}
