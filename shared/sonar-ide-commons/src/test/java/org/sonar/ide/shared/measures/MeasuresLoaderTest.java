package org.sonar.ide.shared.measures;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.ide.test.SonarIdeTestCase;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class MeasuresLoaderTest extends SonarIdeTestCase {
  @BeforeClass
  public static void init() throws Exception {
    SonarIdeTestCase.init();
  }

  @AfterClass
  public static void cleanup() throws Exception {
    SonarIdeTestCase.cleanup();
  }

  @Test
  public void testGetMeasures() throws Exception {
    List<MeasureData> measures = getMeasures(getProject("measures"), "Measures");

    assertThat(measures.size(), greaterThan(0));
  }

  private List<MeasureData> getMeasures(File project, String className) throws Exception {
    return MeasuresLoader.getMeasures(
        getTestServer().getSonar(),
        getProjectKey(project) + ":[default]." + className
    );
  }
}
