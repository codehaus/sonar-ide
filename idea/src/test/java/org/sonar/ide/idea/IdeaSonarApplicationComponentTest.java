package org.sonar.ide.idea;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class IdeaSonarApplicationComponentTest {
  private IdeaSonarApplicationComponent component;

  @Before
  public void setUp() {
    component = new IdeaSonarApplicationComponent();
  }

  @Test
  public void testGetComponentName() {
    assertThat(component.getComponentName(), notNullValue());
  }

  @Test
  public void testGetInspectionClasses() throws Exception {
    assertThat(component.getInspectionClasses(), notNullValue());
    assertThat(component.getInspectionClasses().length, is(1));
  }
}
