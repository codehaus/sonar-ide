package org.sonar.ide.shared.coverage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.ide.test.AbstractSonarIdeTest;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class CoverageLoaderTest extends AbstractSonarIdeTest {
  @BeforeClass
  public static void init() throws Exception {
    AbstractSonarIdeTest.init();
  }

  @AfterClass
  public static void cleanup() throws Exception {
    AbstractSonarIdeTest.cleanup();
  }

  @Test
  public void testGetCoverage() throws Exception {
    CoverageData coverage = getCoverage(getProject("coverage"), "Coverage");
    assertThat(coverage, notNullValue());
    assertThat(coverage.isCovered(1), is(false)); // class
    assertThat(coverage.isCovered(2), is(true));  // method
    assertThat(coverage.isCovered(3), is(true));  // if (false)
    assertThat(coverage.isCovered(4), is(false));
  }

  private CoverageData getCoverage(File project, String className) throws Exception {
    return CoverageLoader.getCoverage(
        getTestServer().getSonar(),
        getProjectKey(project) + ":[default]." + className
    );
  }
}
