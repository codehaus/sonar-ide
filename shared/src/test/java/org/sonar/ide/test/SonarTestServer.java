package org.sonar.ide.test;

import org.mortbay.jetty.testing.ServletTester;
import org.sonar.ide.shared.SonarProperties;
import org.sonar.wsclient.Server;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.Source;
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
    tester.addServlet(ViolationServlet.class, "/api/violations");
    tester.addServlet(SourceServlet.class, "/api/sources");

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
    HttpClient4Connector connector = new HttpClient4Connector(new Server(getBaseUrl()));
    return new Sonar(connector);
  }

  public static void main(String[] args) throws Exception {
    SonarTestServer server = new SonarTestServer();
    server.start();

    SonarProperties properties = SonarProperties.getInstance();
    properties.getServer().setHost(server.getBaseUrl());
    properties.save();

    String key = "test:test:[default].ClassOnDefaultPackage";

    Sonar sonar = server.getSonar();
    ViolationQuery query = new ViolationQuery(key);
    System.out.println(sonar.findAll(query));

    SourceQuery sourceQuery = new SourceQuery(key);
    Source source = sonar.find(sourceQuery);
    System.out.println(source.getLines());

    /*
    String key = "org.codehaus.sonar-ide.test-project:module1:[default].ClassOnDefaultPackage";
    Sonar sonar = Sonar.create("http://localhost:9000");
    SourceQuery sourceQuery = new SourceQuery(key);
    Source source = sonar.find(sourceQuery);
    System.out.println(source.getLines());
    */
  }
}
