package org.sonar.ide.wsclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.ide.test.SonarIdeTestCase;
import org.sonar.ide.test.SonarTestServer;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.connectors.ConnectionException;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.List;

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

  @Test
  public void connectionOverSecuredProxy() throws Exception {
    final String PROXY_USER = "user";
    final String PROXY_PASS = "pass";

    testServer.setProxyAuth(PROXY_USER, PROXY_PASS).start();

    // Install fake ProxySelector
    final ProxySelector defaultProxySelector = ProxySelector.getDefault();
    ProxySelector.setDefault(new ProxySelector() {
      @Override
      public List<Proxy> select(URI uri) {
        try {
          InetAddress addr = InetAddress.getLocalHost();
          int port = testServer.getHttpPort();
          if ("http".equals(uri.getScheme())) {
            return Arrays.asList(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(addr, port)));
          }
          return Arrays.asList(Proxy.NO_PROXY);
        } catch (UnknownHostException e) {
          e.printStackTrace();
          return Arrays.asList(Proxy.NO_PROXY);
        }
      }

      @Override
      public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        ioe.printStackTrace();
      }
    });

    // Install fake Authenticator
    Authenticator.setDefault(new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(PROXY_USER, PROXY_PASS.toCharArray());
      }
    });

    new RemoteSonar(testServer.getHost()).search("");

    ProxySelector.setDefault(defaultProxySelector);
    Authenticator.setDefault(null);
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
