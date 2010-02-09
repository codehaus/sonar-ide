package org.sonar.ide.idea;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class SelectInSonarTargetTest {
  SelectInSonarTarget selectInSonarTarget;

  @Before
  public void setUp() {
    selectInSonarTarget = new SelectInSonarTarget();
  }

  @Test
  public void test() {
    assertThat(selectInSonarTarget.getToolWindowId(), nullValue());
    assertThat(selectInSonarTarget.getMinorViewId(), nullValue());
    assertThat(selectInSonarTarget.toString(), is("Sonar"));
  }
}
