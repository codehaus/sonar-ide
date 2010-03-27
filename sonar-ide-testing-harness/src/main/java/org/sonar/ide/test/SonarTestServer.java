package org.sonar.ide.test;

import org.mortbay.jetty.testing.ServletTester;
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
  }

  public void stop() throws Exception {
    tester.stop();
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
