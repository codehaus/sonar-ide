/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Sonar-IDE is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar-IDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar-IDE; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.test;

import org.mortbay.jetty.LocalConnector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.ResourceQuery;
import org.sonar.wsclient.services.ServerQuery;
import org.sonar.wsclient.services.SourceQuery;
import org.sonar.wsclient.services.ViolationQuery;

/**
 * See {@link org.mortbay.jetty.testing.ServletTester}
 *
 * @author Evgeny Mandrikov
 */
public class SonarTestServer {
  protected static final Logger LOG = LoggerFactory.getLogger(SonarTestServer.class);

  Server _server = new Server();
  LocalConnector _connector = new LocalConnector();
  Context _context = new Context(Context.SESSIONS | Context.SECURITY);

  private int port = -1;
  private String baseUrl;

  public SonarTestServer() {
    _server.setSendServerVersion(false);
    _server.addConnector(_connector);
    _server.addHandler(_context);
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String createSocketConnector() throws Exception {
    SocketConnector connector = new SocketConnector();
    connector.setHost("127.0.0.1");
    if (port != -1) {
      connector.setPort(port);
    }
    _server.addConnector(connector);
    if (_server.isStarted()) {
      connector.start();
    } else {
      connector.open();
    }
    return "http://" + connector.getHost() + ":" + connector.getLocalPort();
  }

  public ServletHolder addServlet(Class servlet, String pathSpec) {
    return _context.addServlet(servlet, pathSpec);
  }

  public void setContextPath(String contextPath) {
    _context.setContextPath(contextPath);
  }

  public void start() throws Exception {
    setContextPath("/");
    addServlet(VersionServlet.class, ServerQuery.BASE_URL);
    addServlet(ViolationServlet.class, ViolationQuery.BASE_URL);
    addServlet(SourceServlet.class, SourceQuery.BASE_URL);
    addServlet(MeasureServlet.class, ResourceQuery.BASE_URL);

    baseUrl = createSocketConnector();
    _server.start();
    LOG.info("Sonar test server started: {}", getBaseUrl());
  }

  public void stop() throws Exception {
    _server.stop();
    LOG.info("Sonar test server stopped: {}", getBaseUrl());
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public Sonar getSonar() {
    HttpClient4Connector connector = new HttpClient4Connector(new Host(getBaseUrl()));
    return new Sonar(connector);
  }

  public static void main(String[] args) throws Exception {
    SonarTestServer server = new SonarTestServer();
    server.setPort(9000);
    server.start();
  }
}
