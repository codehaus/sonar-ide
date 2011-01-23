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

package org.sonar.ide.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Evgeny Mandrikov
 */
public abstract class TestServlet extends GenericServlet {

  private static final String DEFAULT_PACKAGE_NAME = "[default]";
  private static final String PROJECT_RESOURCE_NAME = "PROJECT";

  protected abstract String getResource(String classKey);

  protected File getResourceAsFile(String testName, String classKey) {
    return new File(getBaseDir(this) + "/" + testName + "/" + getResource(classKey));
  }

  @Override
  public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String json;
    try {
      String resourceKey = request.getParameter("resource");
      String[] parts = resourceKey.split(":");
      switch (parts.length) {
        case 2:
          json = loadProjectData(parts[0], parts[1]);
          break;
        case 3:
          json = loadFileData(parts[0], parts[1], parts[2]);
          break;
        default:
          json = "[]";
          break;
      }
    } catch (Exception e) {
      SonarTestServer.LOG.error(e.getMessage(), e);
      json = "[]";
    }
    out.println(json);
  }

  public static String getBaseDir(GenericServlet servlet) {
    return StringUtils.defaultIfEmpty(servlet.getServletConfig().getInitParameter("baseDir"), "target/sonar-data");
  }

  protected String loadFileData(String groupId, String artifactId, String classKey) throws IOException {
    SonarTestServer.LOG.debug("Loading data for {}:{}:{} file", new Object[] { groupId, artifactId, classKey });
    if (classKey.startsWith(DEFAULT_PACKAGE_NAME)) {
      classKey = StringUtils.substringAfter(classKey, ".");
    }
    String testName;
    if (StringUtils.contains(groupId, ".")) {
      testName = StringUtils.substringAfterLast(groupId, ".");
    } else {
      testName = groupId;
    }
    File resourceFile = getResourceAsFile(testName, classKey);
    SonarTestServer.LOG.info("Resource file: {}", resourceFile);
    return FileUtils.readFileToString(resourceFile);
  }

  protected String loadProjectData(String groupId, String artifactId) throws IOException {
    SonarTestServer.LOG.debug("Loading data for {}:{} project", new Object[] { groupId, artifactId });

    // Calculate the test name
    String testName;
    if (StringUtils.contains(groupId, ".")) {
      testName = StringUtils.substringAfterLast(groupId, ".");
    } else {
      testName = groupId;
    }

    // Calculate the resource name
    String resourceName;
    if (testName.equals(artifactId)) {
      // Simple test project.
      resourceName = PROJECT_RESOURCE_NAME;
    } else {
      // Multi-modules test project.
      resourceName = PROJECT_RESOURCE_NAME + "_" + artifactId;
    }

    // Load resource data file.
    File resourceFile = getResourceAsFile(testName, resourceName);
    if (resourceFile.isFile() && resourceFile.canRead()) {
      SonarTestServer.LOG.info("Resource file: {}", resourceFile);
      return FileUtils.readFileToString(resourceFile);
    } else {
      SonarTestServer.LOG.info("Resource file not exist : {}", resourceFile);
      return "[]";
    }
  }
}
