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
    this(host, "", "");
  }

  public SonarClient(String host, String username, String password) {
    super(ConnectorFactory.create(new Host(host, username, password)));
    connect();
  }

  private void connect() {
    try {
      ServerQuery serverQuery = new ServerQuery();
      Server server = find(serverQuery);
      available = checkVersion(server);
    } catch (ConnectionException e) {
      available = false;
    }
  }

  private boolean checkVersion(Server server) {
    if (server == null) {
      return false;
    }
    String version = server.getVersion();
    return version != null && version.startsWith("2.");
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
