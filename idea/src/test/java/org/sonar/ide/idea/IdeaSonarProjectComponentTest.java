package org.sonar.ide.idea;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
@Ignore("Not ready yet")
public class IdeaSonarProjectComponentTest {
  private IdeaSonarProjectComponent component;

  @Before
  public void setUp() {
    component = new IdeaSonarProjectComponent(null);
  }

  @Test
  public void testGetComponentName() {
    assertThat(component.getComponentName(), notNullValue());
  }
}
