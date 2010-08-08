package org.sonar.ide.wsclient;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.sonar.ide.api.SourceCode;
import org.sonar.ide.api.SourceCodeDiffEngine;
import org.sonar.ide.api.SourceCodeSearchEngine;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient3Connector;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.MetricQuery;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * EXPERIMENTAL!!!
 * Layer between Sonar IDE and Sonar based on sonar-ws-client :
 * Sonar IDE -> RemoteSonarIndex -> sonar-ws-client -> Sonar
 *
 * @author Evgeny Mandrikov
 * @since 0.2
 */
class RemoteSonarIndex implements SourceCodeSearchEngine {

  private static final int TIMEOUT_MS = 30000;
  private static final int MAX_TOTAL_CONNECTIONS = 40;
  private static final int MAX_HOST_CONNECTIONS = 4;

  private final Host host;
  private final Sonar sonar;
  private final SourceCodeDiffEngine diffEngine;

  public RemoteSonarIndex(Host host, SourceCodeDiffEngine diffEngine) {
    this(host, new Sonar(configureConnector(host)), diffEngine);
  }

  /**
   * TODO Godin: looks like we have a bug in sonar-ws-client
   * <ul>
   * <li>
   * I suppose that call of method {@link HttpClient3Connector#configureCredentials()} should be added to
   * constructor {@link HttpClient3Connector#HttpClient3Connector(Host, HttpClient)}
   * </li>
   * <li>
   * Also {@link HttpConnectionManagerParams#setSoTimeout(int)} should be used.
   * </li>
   * </ul>
   *
   * @param server Sonar server
   * @return configured {@link HttpClient3Connector}
   */
  private static HttpClient3Connector configureConnector(Host server) {
    // createClient
    final HttpConnectionManagerParams params = new HttpConnectionManagerParams();
    params.setSoTimeout(TIMEOUT_MS);
    params.setConnectionTimeout(TIMEOUT_MS);
    params.setDefaultMaxConnectionsPerHost(MAX_HOST_CONNECTIONS);
    params.setMaxTotalConnections(MAX_TOTAL_CONNECTIONS);
    final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
    connectionManager.setParams(params);
    HttpClient httpClient = new HttpClient(connectionManager);
    // configureCredentials
    if (server.getUsername() != null) {
      httpClient.getParams().setAuthenticationPreemptive(true);
      Credentials defaultcreds = new UsernamePasswordCredentials(server.getUsername(), server.getPassword());
      httpClient.getState().setCredentials(AuthScope.ANY, defaultcreds);
    }

    // proxy
    // TODO SONAR-1586
//    httpClient.getHostConfiguration().setProxy("localhost", 9000);
//    Credentials proxyCredentials = new UsernamePasswordCredentials("user", "pass");
//    httpClient.getState().setProxyCredentials(AuthScope.ANY, proxyCredentials);

    return new HttpClient3Connector(server, httpClient);
  }

  private RemoteSonarIndex(Host host, Sonar sonar, SourceCodeDiffEngine diffEngine) {
    this.sonar = sonar;
    this.diffEngine = diffEngine;
    this.host = host;
  }

  /**
   * {@inheritDoc}
   */
  public SourceCode search(String key) {
    Resource resource = sonar.find(new ResourceQuery().setResourceKeyOrId(key));
    if (resource == null) {
      return null;
    }
    return new RemoteSourceCode(key).setRemoteSonarIndex(this);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<SourceCode> getProjects() {
    ArrayList<SourceCode> result = Lists.newArrayList();
    for (Resource resource : sonar.findAll(new ResourceQuery())) {
      result.add(new RemoteSourceCode(resource.getKey(), resource.getName()).setRemoteSonarIndex(this));
    }
    return result;
  }

  protected Host getServer() {
    return host;
  }

  protected Sonar getSonar() {
    return sonar;
  }

  protected SourceCodeDiffEngine getDiffEngine() {
    return diffEngine;
  }

  public Map<String, Metric> getMetrics() {
    // TODO Godin: This is not optimal. Would be better to load metrics only once.
    List<Metric> metrics = getSonar().findAll(MetricQuery.all());
    return Maps.uniqueIndex(metrics, new Function<Metric, String>() {
      public String apply(Metric metric) {
        return metric.getKey();
      }
    });
  }

}
