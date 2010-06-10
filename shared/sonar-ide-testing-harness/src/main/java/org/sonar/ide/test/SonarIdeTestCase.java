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

package org.sonar.ide.test;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Base class for Sonar-IDE tests.
 *
 * @author Evgeny Mandrikov
 */
public abstract class SonarIdeTestCase {
  private static final Logger LOG = LoggerFactory.getLogger(SonarIdeTestCase.class);

  private static final ReadWriteLock copyProjectLock = new ReentrantReadWriteLock();

  protected static File projectsSource;

  protected static File projectsWorkdir;

  protected static SonarTestServer testServer;

//  @BeforeClass

  public static void init() throws Exception {
    projectsSource = new File("target/projects-source");
    projectsWorkdir = new File("target/projects-target");
  }

  /**
   * Stops {@link org.sonar.ide.test.SonarTestServer} if started.
   *
   * @throws Exception if something wrong
   */
//  @AfterClass
  public static void cleanup() throws Exception {
    if (testServer != null) {
      testServer.stop();
      testServer = null;
    }
  }

  /**
   * @return instance of {@link org.sonar.ide.test.SonarTestServer}
   * @throws Exception if something wrong
   */
  protected SonarTestServer getTestServer() throws Exception {
    if (testServer == null) {
      testServer = new SonarTestServer();
      testServer.start();
    }
    return testServer;
  }

  /**
   * @param project project (see {@link #getProject(String)})
   * @return Sonar key for specified project
   */
  protected static String getProjectKey(File project) {
    String projectName = project.getName();
    return "org.sonar-ide.tests." + projectName + ":" + projectName;
  }

  /**
   * @param project  project (see {@link #getProject(String)})
   * @param filename filename
   * @return specified file from specified project
   */
  protected static File getProjectFile(File project, String filename) {
    return new File(project, filename);
  }

  /**
   * @param projectName name of project
   * @return project directory
   * @throws IOException if unable to prepare project directory
   */
  protected static File getProject(String projectName) throws IOException {
    File destDir = new File(projectsWorkdir, projectName); // TODO include testName
    return getProject(projectName, destDir);
  }

  /**
   * Installs specified project to specified directory.
   *
   * @param projectName name of project
   * @param destDir     destination directory
   * @return project directory
   * @throws IOException if unable to prepare project directory
   */
  protected static File getProject(String projectName, File destDir) throws IOException {
    copyProjectLock.writeLock().lock();
    try {
      File projectFolder = new File(projectsSource, projectName);
      Assert.assertTrue(
          "Project " + projectName + " folder not found.\n" + projectFolder.getAbsolutePath(),
          projectFolder.isDirectory()
      );
      if (destDir.isDirectory()) {
        LOG.warn("Directory for project already exists: {}", destDir);
      }

      // TODO interpolate files
//      FileUtils.copyDirectory(projectFolder, destDir, HiddenFileFilter.VISIBLE);
      FileUtils.copyDirectory(projectFolder, destDir);
      return destDir;
    } finally {
      copyProjectLock.writeLock().unlock();
    }
  }

}
