package org.sonar.ide.shared;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.ide.test.SonarTestServer;
import org.sonar.wsclient.services.Violation;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Evgeny Mandrikov
 */
public class ViolationsLoaderTest {
  private SonarTestServer testServer;
  private ViolationsLoader loader;

  @Before
  public void setUp() throws Exception {
    testServer = new SonarTestServer();
    testServer.start();
    loader = new ViolationsLoader();
  }

  @After
  public void tearDown() throws Exception {
    testServer.stop();
  }

  @Test
  public void testGetViolations() throws Exception {
    Collection<Violation> violations = loader.getViolations(testServer.getSonar(), "test");
    assertEquals(4, violations.size());
  }
}
