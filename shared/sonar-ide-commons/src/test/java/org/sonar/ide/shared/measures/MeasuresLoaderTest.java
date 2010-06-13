package org.sonar.ide.shared.measures;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.sonar.ide.test.SonarIdeTestCase;

/**
 * @author Evgeny Mandrikov
 */
public class MeasuresLoaderTest extends SonarIdeTestCase {

  @Test
  public void testGetMeasures() throws Exception {
    List<MeasureData> measures = getMeasures(getProject("measures"), "Measures");

    assertThat(measures.size(), greaterThan(0));
  }

  @Test
  @Ignore("Not ready")
  @SuppressWarnings("unchecked")
  public void notLoadDataMeasures() throws Exception {
    List measures = getMeasures(getProject("measures"), "Measures");

    assertThat((List<Object>) measures, not(hasItem(hasProperty("name", is("Coverage hits data")))));
  }

  private List<MeasureData> getMeasures(File project, String className) throws Exception {
    return MeasuresLoader.getMeasures(getTestServer().getSonar(), getProjectKey(project) + ":[default]." + className);
  }
}
