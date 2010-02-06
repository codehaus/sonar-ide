package org.sonar.ide.shared;

import org.junit.Test;
import org.sonar.wsclient.services.Violation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Evgeny Mandrikov
 */
public class ViolationUtilsTest {
  @Test
  public void testConvertPriority() throws Exception {
    assertEquals(0, ViolationUtils.convertPriority("Blocker"));
    assertEquals(1, ViolationUtils.convertPriority("Critical"));
    assertEquals(2, ViolationUtils.convertPriority("Major"));
    assertEquals(3, ViolationUtils.convertPriority("Minor"));
    assertEquals(4, ViolationUtils.convertPriority("Info"));
    assertEquals(4, ViolationUtils.convertPriority("Other"));
  }

  @Test
  public void testSortByPriority() throws Exception {
    Violation minor = newViolation("Minor", 1);
    Violation blocker = newViolation("Blocker", 1);

    List<Violation> violations = ViolationUtils.sortByPriority(Arrays.asList(minor, blocker));

    assertSame(blocker, violations.get(0));
    assertSame(minor, violations.get(1));
  }

  @Test
  public void testDivideByLines() throws Exception {
    Violation v1 = newViolation("Minor", 2);
    Violation v2 = newViolation("Minor", 1);
    Violation v3 = newViolation("Blocker", 2);

    Map<Integer, List<Violation>> map = ViolationUtils.splitByLines(Arrays.asList(v1, v2, v3));

    assertEquals(2, map.size());
    assertEquals(1, map.get(1).size());
    assertEquals(2, map.get(2).size());
  }

  @Test
  public void testGetDescription() {
    Violation violation = mock(Violation.class);
    when(violation.getRuleName()).thenReturn("Unused");
    when(violation.getMessage()).thenReturn("Avoid unused");
    assertEquals("Unused : Avoid unused", ViolationUtils.getDescription(violation));
  }

  protected Violation newViolation(String priority, int line) {
    Violation violation = new Violation();
    violation.setPriority(priority);
    violation.setLine(line);
    return violation;
  }
}
