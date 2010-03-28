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
