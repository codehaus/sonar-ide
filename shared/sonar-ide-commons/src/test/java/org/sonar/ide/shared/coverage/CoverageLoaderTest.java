package org.sonar.ide.shared.coverage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.ide.test.AbstractSonarIdeTest;

import java.io.File;

import static org.hamcrest.Matchers.*;
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
    assertThat(coverage.getLineHits(1), equalTo(-1));             // class
    assertThat(coverage.getLineHits(2), greaterThanOrEqualTo(1)); // method
    assertThat(coverage.getLineHits(3), greaterThanOrEqualTo(1)); // if (false) {
    assertThat(coverage.getLineHits(4), equalTo(0));              //   System.out.println("Never");
    assertThat(coverage.getLineHits(5), equalTo(-1));             // }
  }

  private CoverageData getCoverage(File project, String className) throws Exception {
    return CoverageLoader.getCoverage(
        getTestServer().getSonar(),
        getProjectKey(project) + ":[default]." + className
    );
  }
}
