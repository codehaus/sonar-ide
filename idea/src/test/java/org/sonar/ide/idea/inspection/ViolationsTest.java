package org.sonar.ide.idea.inspection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
@Ignore("Not ready yet")
public class ViolationsTest {
  private Violations inspection;

  @Before
  public void setUp() {
    inspection = new Violations();
  }

  @Test
  public void testGetDisplayName() throws Exception {
    assertThat(inspection.getDisplayName(), notNullValue());
  }

  @Test
  public void testGetShortName() throws Exception {
    assertThat(inspection.getShortName(), notNullValue());
  }
}
