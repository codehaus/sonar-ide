package org.sonar.ide.idea;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class IdeaSonarModuleComponentTest {
  private IdeaSonarModuleComponent component;

  @Before
  public void setUp() {
    component = new IdeaSonarModuleComponent(null);
  }

  @Test
  public void testGetComponentName() {
    assertThat(component.getComponentName(), notNullValue());
  }
}
