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
    assertThat(coverage.getCoverageStatus(1), is(CoverageData.CoverageStatus.NO_DATA));           // class
    assertThat(coverage.getCoverageStatus(2), is(CoverageData.CoverageStatus.FULLY_COVERED));     // method
    assertThat(coverage.getCoverageStatus(3), is(CoverageData.CoverageStatus.PARTIALLY_COVERED)); // if (false) {
    assertThat(coverage.getCoverageStatus(4), is(CoverageData.CoverageStatus.UNCOVERED));         //   System.out.println("Never");
    assertThat(coverage.getCoverageStatus(5), is(CoverageData.CoverageStatus.NO_DATA));           // }
  }

  private CoverageData getCoverage(File project, String className) throws Exception {
    return CoverageLoader.getCoverage(
        getTestServer().getSonar(),
        getProjectKey(project) + ":[default]." + className
    );
  }
}
