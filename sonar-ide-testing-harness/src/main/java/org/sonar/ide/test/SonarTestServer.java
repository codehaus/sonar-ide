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

import org.mortbay.jetty.testing.ServletTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.ServerQuery;
import org.sonar.wsclient.services.SourceQuery;
import org.sonar.wsclient.services.ViolationQuery;

/**
 * @author Evgeny Mandrikov
 */
public class SonarTestServer {
  protected static final Logger LOG = LoggerFactory.getLogger(SonarTestServer.class);

  private ServletTester tester;
  private String baseUrl;

  public void start() throws Exception {
    tester = new ServletTester();
    tester.setContextPath("/");
    tester.addServlet(VersionServlet.class, ServerQuery.BASE_URL);
    tester.addServlet(ViolationServlet.class, ViolationQuery.BASE_URL);
    tester.addServlet(SourceServlet.class, SourceQuery.BASE_URL);

    baseUrl = tester.createSocketConnector(true);
    tester.start();
    LOG.info("Sonar test server started: {}", getBaseUrl());
  }

  public void stop() throws Exception {
    tester.stop();
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
    server.start();
  }
}
