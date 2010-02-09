package org.sonar.ide.client;

import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.ConnectionException;
import org.sonar.wsclient.connectors.ConnectorFactory;
import org.sonar.wsclient.services.Model;
import org.sonar.wsclient.services.Query;
import org.sonar.wsclient.services.Server;
import org.sonar.wsclient.services.ServerQuery;

import java.util.Collection;

/**
 * @author Evgeny Mandrikov
 */
public class SonarClient extends Sonar {
  private boolean available;
  private int serverTrips = 0;

  public SonarClient(String host) {
    super(ConnectorFactory.create(new Host(host)));
    connect();
  }

  private void connect() {
    try {
      ServerQuery serverQuery = new ServerQuery();
      Server server = find(serverQuery);
      available = checkVersion(server.getVersion());
    } catch (ConnectionException e) {
      available = false;
    }
  }

  private boolean checkVersion(String version) {
    return version.equalsIgnoreCase("1.13-SNAPSHOT") || version.equalsIgnoreCase("2.0");
  }

  @Override
  public <MODEL extends Model> MODEL find(Query<MODEL> query) {
    serverTrips++;
    return super.find(query);
  }

  @Override
  public <MODEL extends Model> Collection<MODEL> findAll(Query<MODEL> query) {
    serverTrips++;
    return super.findAll(query);
  }

  public int getServerTrips() {
    return serverTrips;
  }

  public boolean isAvailable() {
    return available;
  }
}
