/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.wsclient;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.sonar.wsclient.services.Violation;

/**
 * @author Evgeny Mandrikov
 */
public class ViolationsTest extends AbstractRemoteTestCase {
  private List<Violation> getViolations(File project, String className) throws Exception {
    return getRemoteSonar().search(getProjectKey(project) + ":[default]." + className).setLocalContent(
        FileUtils.readFileToString(getProjectFile(project, "/src/main/java/" + className + ".java"))).getViolations();
  }

  @Test
  public void testGetViolations() throws Exception {
    List<Violation> violations = getViolations(getProject("SimpleProject"), "ClassOnDefaultPackage");

    assertThat(violations.size(), is(4));
    // TODO assert lines
  }

  /**
   * See <a href="http://jira.codehaus.org/browse/SONARIDE-52">SONARIDE-52</a>
   */
  @Test
  public void testViolationOnFile() throws Exception {
    List<Violation> violations = getViolations(getProject("SimpleProject"), "ViolationOnFile");

    assertThat(violations.size(), is(0));
  }

  /**
   * See <a href="http://jira.codehaus.org/browse/SONARIDE-13">SONARIDE-13</a>
   */
  @Test
  public void testCodeChanged() throws Exception {
    List<Violation> violations = getViolations(getProject("code-changed"), "CodeChanged");

    assertThat(violations.size(), is(1));
    assertThat(violations.get(0).getLine(), is(4));
  }

  @Test
  public void testLineForViolationDoesntExists() throws Exception {
    List<Violation> violations = getViolations(getProject("code-changed"), "LineForViolationDoesntExists");

    assertThat(violations.size(), is(1));
    assertThat(violations.get(0).getLine(), is(2));
  }

  @Test
  public void testMoreThanOneMatch() throws Exception {
    List<Violation> violations = getViolations(getProject("code-changed"), "MoreThanOneMatch");

    assertThat(violations.size(), is(2));
    assertThat(violations.get(0).getLine(), is(4));
    assertThat(violations.get(1).getLine(), is(4));
  }
}
