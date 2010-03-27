package org.sonar.ide.test;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Base class for Sonar-IDE tests.
 *
 * @author Evgeny Mandrikov
 */
public class AbstractSonarIdeTest {
  private static final ReadWriteLock copyProjectLock = new ReentrantReadWriteLock();

  protected static File projectsSource;

  protected static File projectsWorkdir;

  protected static SonarTestServer testServer;

//  @BeforeClass

  public static void init() {
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

      // TODO interpolate files
//      FileUtils.copyDirectory(projectFolder, destDir, HiddenFileFilter.VISIBLE);
      FileUtils.copyDirectory(projectFolder, destDir);
      return destDir;
    } finally {
      copyProjectLock.writeLock().unlock();
    }
  }

}
