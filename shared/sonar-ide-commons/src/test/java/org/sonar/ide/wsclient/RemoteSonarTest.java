package org.sonar.ide.wsclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sonar.ide.test.SonarIdeTestCase;
import org.sonar.ide.test.SonarTestServer;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.connectors.ConnectionException;

/**
 * @author Evgeny Mandrikov
 */
public class RemoteSonarTest extends SonarIdeTestCase {

  private static final long LATENCY = 2 * 30000; // 2 * HttpClient3Connector.TIMEOUT_MS; 

  private SonarTestServer testServer;

  @Before
  public void setUp() throws Exception {
    testServer = new SonarTestServer();
  }

  @After
  public void tearDown() {
    testServer.stop();
  }

  @Ignore("not supported")
  @Test
  public void connectionOverSecuredProxy() throws Exception {
    testServer.setHttpPort(9000).setProxyAuth("user", "pass").start();

    new RemoteSonar(testServer.getHost()).search("");
  }

  /**
   * See {@link RemoteSonarIndex#configureConnector(org.sonar.wsclient.Host)}
   */
  @Test(expected = ConnectionException.class)
  public void readTimeout() throws Exception {
    testServer.setLatency(LATENCY).start();

    new RemoteSonar(testServer.getHost()).search("");
  }

  @Test(expected = ConnectionException.class)
  public void noServerStarted() throws Exception {
    new RemoteSonar(new Host("http://localhost:70")).search("");
  }

  @Test(expected = ConnectionException.class)
  public void unauthorized() throws Exception {
    testServer.addUser("user", "pass", "admin").addSecuredRealm("/*", "admin").start();

    new RemoteSonar(testServer.getHost()).search("");
  }

  @Test
  public void authorized() throws Exception {
    testServer.addUser("user", "pass", "admin").addSecuredRealm("/*", "admin").start();

    new RemoteSonar(new Host(testServer.getBaseUrl()).setUsername("user").setPassword("pass"))
        .search("");
  }

}
