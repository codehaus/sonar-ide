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

  private Server server = new Server();
  private Context context = new Context(Context.SESSIONS | Context.SECURITY);

  private String baseUrl;
  private String baseDir;

  /**
   * @throws Exception if something wrong
   */
  public SonarTestServer() throws Exception {
    this(-1, "");
  }

  /**
   * @param port    port
   * @param baseDir location of sonar-data directory
   * @throws Exception if something wrong
   */
  public SonarTestServer(int port, String baseDir) throws Exception {
    this.baseDir = baseDir;

    server.setSendServerVersion(false);
    server.addConnector(new LocalConnector());
    server.addHandler(context);

    setContextPath("/");
    addServlet(VersionServlet.class, ServerQuery.BASE_URL);
    addServlet(ViolationServlet.class, ViolationQuery.BASE_URL);
    addServlet(SourceServlet.class, SourceQuery.BASE_URL);
    addServlet(MeasureServlet.class, ResourceQuery.BASE_URL);
    baseUrl = createSocketConnector(port);
  }

  /**
   * @param port port
   * @return a URL to access the server via the socket connector
   * @throws Exception if something wrong
   * @see org.mortbay.jetty.testing.ServletTester#createSocketConnector(boolean)
   */
  public String createSocketConnector(int port) throws Exception {
    SocketConnector connector = new SocketConnector();
    connector.setHost("127.0.0.1");
    if (port != -1) {
      connector.setPort(port);
    }
    server.addConnector(connector);
    if (server.isStarted()) {
      connector.start();
    } else {
      connector.open();
    }
    return "http://" + connector.getHost() + ":" + connector.getLocalPort();
  }

  /**
   * @param servlet  servlet
   * @param pathSpec path
   * @return servlet holder
   * @see org.mortbay.jetty.servlet.Context#addServlet(java.lang.Class, java.lang.String)
   */
  public ServletHolder addServlet(Class servlet, String pathSpec) {
    ServletHolder servletHolder = context.addServlet(servlet, pathSpec);
    servletHolder.setInitParameter("baseDir", baseDir);
    return servletHolder;
  }

  /**
   * @param contextPath context path
   * @see org.mortbay.jetty.handler.ContextHandler#setContextPath(java.lang.String)
   */
  public void setContextPath(String contextPath) {
    context.setContextPath(contextPath);
  }

  /**
   * @throws Exception if something wrong
   * @see org.mortbay.jetty.testing.ServletTester#start()
   */
  public void start() throws Exception {
    server.start();
    LOG.info("Sonar test server started: {}", getBaseUrl());
  }

  /**
   * @throws Exception if something wrong
   * @see org.mortbay.jetty.testing.ServletTester#stop()
   */
  public void stop() throws Exception {
    server.stop();
    LOG.info("Sonar test server stopped: {}", getBaseUrl());
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public Sonar getSonar() {
    HttpClient4Connector connector = new HttpClient4Connector(new Host(getBaseUrl()));
    return new Sonar(connector);
  }

  /**
   * Entry point.
   *
   * @param args command-line arguments
   * @throws Exception if something wrong
   */
  public static void main(String[] args) throws Exception {
    new SonarTestServer(9000, "sonar-data").start();
  }
}
