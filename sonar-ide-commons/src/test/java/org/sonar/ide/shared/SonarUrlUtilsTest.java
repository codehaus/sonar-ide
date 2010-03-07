package org.sonar.ide.shared;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class SonarUrlUtilsTest {
  private static final String HOST = "http://localhost";
  private static final String KEY = "test:test";

  @Test
  public void testGetDashboard() throws Exception {
    assertThat(SonarUrlUtils.getDashboard(HOST, null), is("http://localhost/project/index/?"));
    assertThat(SonarUrlUtils.getDashboard(HOST, KEY), is("http://localhost/project/index/test:test?"));
  }

  @Test
  public void testGetComponents() throws Exception {
    assertThat(SonarUrlUtils.getComponents(HOST, null), is("http://localhost/components/index/?"));
    assertThat(SonarUrlUtils.getComponents(HOST, KEY), is("http://localhost/components/index/test:test?"));
  }

  @Test
  public void testGetTimemachine() throws Exception {
    assertThat(SonarUrlUtils.getTimemachine(HOST, null), is("http://localhost/timemachine/index/?"));
    assertThat(SonarUrlUtils.getTimemachine(HOST, KEY), is("http://localhost/timemachine/index/test:test?"));
  }

  @Test
  public void testGetResource() throws Exception {
    assertThat(SonarUrlUtils.getResource(HOST, null), is("http://localhost/resource/index/?"));
    assertThat(SonarUrlUtils.getResource(HOST, KEY), is("http://localhost/resource/index/test:test?"));
  }

  @Test
  public void testGetMeasuresDrilldown() throws Exception {
    assertThat(SonarUrlUtils.getMeasuresDrilldown(HOST, null), is("http://localhost/drilldown/measures/?"));
    assertThat(SonarUrlUtils.getMeasuresDrilldown(HOST, KEY), is("http://localhost/drilldown/measures/test:test?"));

    assertThat(SonarUrlUtils.getMeasuresDrilldown(HOST, KEY, null), is("http://localhost/drilldown/measures/test:test?"));
    assertThat(SonarUrlUtils.getMeasuresDrilldown(HOST, KEY, "classes"), is("http://localhost/drilldown/measures/test:test?metric=classes&"));
  }

  @Test
  public void testGetViolationsDrilldown() throws Exception {
    assertThat(SonarUrlUtils.getViolationsDrilldown(HOST, null), is("http://localhost/drilldown/violations/?"));
    assertThat(SonarUrlUtils.getViolationsDrilldown(HOST, KEY), is("http://localhost/drilldown/violations/test:test?"));
  }
}
