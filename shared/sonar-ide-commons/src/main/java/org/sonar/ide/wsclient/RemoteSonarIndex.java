package org.sonar.ide.wsclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.sonar.ide.api.SourceCode;
import org.sonar.ide.api.SourceCodeDiffEngine;
import org.sonar.ide.api.SourceCodeSearchEngine;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.ConnectorFactory;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.MetricQuery;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * EXPERIMENTAL!!!
 * Layer between Sonar IDE and Sonar based on sonar-ws-client :
 * Sonar IDE -> RemoteSonarIndex -> sonar-ws-client -> Sonar
 * 
 * @author Evgeny Mandrikov
 * @since 0.2
 */
class RemoteSonarIndex implements SourceCodeSearchEngine {

  private final Host host;
  private final Sonar sonar;
  private final SourceCodeDiffEngine diffEngine;

  /**
   * @deprecated use {@link #RemoteSonarIndex(Host, Sonar, SourceCodeDiffEngine)} instead of it
   */
  @Deprecated
  public RemoteSonarIndex(Sonar sonar, SourceCodeDiffEngine diffEngine) {
    this(null, sonar, diffEngine);
  }

  public RemoteSonarIndex(Host host, SourceCodeDiffEngine diffEngine) {
    this(host, new Sonar(ConnectorFactory.create(host)), diffEngine);
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
