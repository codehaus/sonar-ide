package org.sonar.ide.test;

import org.mortbay.jetty.testing.ServletTester;
import org.sonar.ide.shared.SonarProperties;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.*;

import java.util.Collection;

/**
 * @author Evgeny Mandrikov
 */
public class SonarTestServer {
  private ServletTester tester;
  private String baseUrl;

  public void start() throws Exception {
    tester = new ServletTester();
    tester.setContextPath("/");
    tester.addServlet(VersionServlet.class, "/api/server/version");
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
    HttpClient4Connector connector = new HttpClient4Connector(new Host(getBaseUrl()));
    return new Sonar(connector);
  }

  public static void main(String[] args) throws Exception {
    Sonar sonar = Sonar.create("http://nemo.sonarsource.org");

    /*
MetricKey: coverage_line_hits_data
Data:34=4;38=0;43=4;45=4;46=4;47=4;48=4;50=4;51=4;52=4;54=4;55=4;56=4;57=4;58=4;59=4;63=4;65=4;66=4;67=12;68=8;69=8;70=1;73=28;74=21;75=21;76=21;79=8;80=8;81=4
MetricKey: branch_coverage_hits_data
Data:67=100%;69=100%;73=100%
     */
    ResourceQuery resourceQuery = new ResourceQuery("org.codehaus.sonar.plugins:sonar-plugin-core:org.sonar.plugins.core.charts.DistributionAreaChart");
    resourceQuery.setMetrics("coverage_line_hits_data", "branch_coverage_hits_data");
    Collection<Resource> resources = sonar.findAll(resourceQuery);
    for (Resource resource : resources) {
      for (Measure measure : resource.getMeasures()) {
        System.out.println("MetricKey: " + measure.getMetricKey());
        System.out.println("Data:" + measure.getData());
      }
    }
    /*
    resourceQuery.setMetrics("duplications_data");
    Collection<Resource> resources = sonar.findAll(resourceQuery);
    for (Resource resource : resources) {
      System.out.println(resource.getMeasures().get(0).getData());
    }
    */

    SonarTestServer server = new SonarTestServer();
    server.start();

    SonarProperties properties = SonarProperties.getInstance();
    properties.getServer().setHost(server.getBaseUrl());
    properties.save();

    String key = "test:test:[default].ClassOnDefaultPackage";

    sonar = server.getSonar();
    ViolationQuery query = new ViolationQuery(key);
    System.out.println(sonar.findAll(query));

    SourceQuery sourceQuery = new SourceQuery(key);
    Source source = sonar.find(sourceQuery);
    System.out.println(source.getLines());

    /* TODO SONARIDE-14
    VersionQuery versionQuery = new VersionQuery();
    System.out.println(sonar.find(versionQuery).getVersion());
    */

    /*
    String key = "org.codehaus.sonar-ide.test-project:module1:[default].ClassOnDefaultPackage";
    Sonar sonar = Sonar.create("http://localhost:9000");
    SourceQuery sourceQuery = new SourceQuery(key);
    Source source = sonar.find(sourceQuery);
    System.out.println(source.getLines());
    */
  }
}
