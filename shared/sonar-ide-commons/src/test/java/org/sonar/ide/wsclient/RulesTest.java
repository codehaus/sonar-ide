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
import org.sonar.wsclient.services.Rule;

/**
 * @author Jérémie Lagarde
 * @since 0.3
 */
public class RulesTest extends AbstractRemoteTestCase {

  private List<Rule> getRules(File project, String className) throws Exception {
    return getRemoteSonar().search(getProjectKey(project) + ":[default]." + className)
        .setLocalContent(FileUtils.readFileToString(getProjectFile(project, "/src/main/java/" + className + ".java"))).getRules();
  }

  @Test
  public void testGetRules() throws Exception {
    List<Rule> rules = getRules(getProject("profile"), "Profile");

    assertThat(rules.size(), is(742));
    // TODO assert lines
  }
}