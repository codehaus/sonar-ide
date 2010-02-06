package org.sonar.ide.shared;

import org.junit.Test;
import org.sonar.ide.test.SonarTestServer;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.ConnectionException;
import org.sonar.wsclient.services.Source;
import org.sonar.wsclient.services.Violation;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Evgeny Mandrikov
 */
public class ViolationsLoaderTest {
  private static final String RESOURCE_KEY = "test:test:[default].ClassOnDefaultPackage";

  private static final String[] LINES = {
      "/* This comment was added after analyze. */",
      "public class ClassOnDefaultPackage {",
      "",
      "  public ClassOnDefaultPackage(int i) {",
      "    /* Next line was changed */",
      "    //int j = i++;",
      "  }",
      "",
      "  /* Next method was renamed */",
      "  private String myMethod2() {",
      "    return \"hello\";",
      "  }",
      "}"
  };

  @Test(expected = ConnectionException.class)
  public void testServerUnavailable() throws Exception {
    ViolationsLoader.getViolations(Sonar.create("http://localhost:9999"), RESOURCE_KEY, LINES);
  }

  @Test
  public void testGetDescription() {
    Violation violation = mock(Violation.class);
    when(violation.getRuleName()).thenReturn("Unused");
    when(violation.getMessage()).thenReturn("Avoid unused");
    assertEquals("Unused : Avoid unused", ViolationsLoader.getDescription(violation));
  }

  @Test
  public void testGetHashCode() {
    int hash1 = ViolationsLoader.getHashCode("int\ti;");
    int hash2 = ViolationsLoader.getHashCode("int i;");
    assertEquals(hash1, hash2);
  }

  @Test
  public void testConvertLines() {
    Violation violation = mock(Violation.class);
    when(violation.getLine()).thenReturn(1);
    Source source = new Source();
    source.addLine(1, LINES[1]);
    ViolationsLoader.convertLines(
        Collections.singleton(violation),
        source,
        LINES
    );
    verify(violation).getLine();
    verify(violation).setLine(2);
    verifyNoMoreInteractions(violation);
  }

  @Test
  public void testGetViolations() throws Exception {
    SonarTestServer testServer = new SonarTestServer();
    testServer.start();

    List<Violation> violations = ViolationsLoader.getViolations(testServer.getSonar(), RESOURCE_KEY, LINES);

    assertEquals(1, violations.size());
    assertEquals((Integer) 2, violations.get(0).getLine());

    testServer.stop();
  }
}
