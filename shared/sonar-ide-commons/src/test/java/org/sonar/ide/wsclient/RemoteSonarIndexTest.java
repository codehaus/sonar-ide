/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.wsclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonar.ide.client.ExtendedHttpClient3Connector;
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

  private static final long TIMEOUT = ExtendedHttpClient3Connector.TIMEOUT_MS * 2;

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
