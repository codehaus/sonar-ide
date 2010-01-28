package org.sonar.ide.test;

import org.mortbay.jetty.testing.ServletTester;
import org.sonar.ide.shared.SonarProperties;
import org.sonar.wsclient.Sonar;
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
    tester.addServlet(ViolationServlet.class, "/api/violations");

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
    return Sonar.create(getBaseUrl());
  }

  public static void main(String[] args) throws Exception {
    SonarTestServer server = new SonarTestServer();
    server.start();

    SonarProperties properties = SonarProperties.getInstance();
    properties.getServer().setHost(server.getBaseUrl());
    properties.save();

    Sonar sonar = Sonar.create(server.getBaseUrl());
    ViolationQuery query = new ViolationQuery("");
    System.out.println(sonar.findAll(query));
  }
}
