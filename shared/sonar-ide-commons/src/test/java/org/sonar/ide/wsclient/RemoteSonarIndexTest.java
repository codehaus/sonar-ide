package org.sonar.ide.wsclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
public class RemoteSonarIndexTest {

  private static final long TIMEOUT = HttpClient3ConnectorFactory.TIMEOUT_MS * 2;

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
  public void connectionOverSecuredHttpProxy() throws Exception {
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

    new RemoteSonarIndex(testServer.getHost()).getMetrics();

    ProxySelector.setDefault(defaultProxySelector);
    Authenticator.setDefault(null);
  }

  @Test(expected = ConnectionException.class)
  public void proxyAuthFailed() throws Exception {
    testServer.setProxyAuth("user", "pass").start();

    new RemoteSonarIndex(testServer.getHost()).getMetrics();
  }

  @Test(expected = ConnectionException.class, timeout = TIMEOUT)
  public void readTimeout() throws Exception {
    testServer.setLatency(TIMEOUT).start();

    new RemoteSonarIndex(testServer.getHost()).getMetrics();
  }

  @Test(expected = ConnectionException.class)
  public void noServerStarted() throws Exception {
    new RemoteSonarIndex(new Host("http://localhost:70")).getMetrics();
  }

  @Test(expected = ConnectionException.class)
  public void unauthorized() throws Exception {
    testServer.addUser("user", "pass", "admin").addSecuredRealm("/*", "admin").start();

    new RemoteSonarIndex(testServer.getHost()).getMetrics();
  }

  @Test
  public void authorized() throws Exception {
    testServer.addUser("user", "pass", "admin").addSecuredRealm("/*", "admin").start();

    new RemoteSonarIndex(testServer.getHost().setUsername("user").setPassword("pass")).getMetrics();
  }

}
