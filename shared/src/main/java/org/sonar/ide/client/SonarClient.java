package org.sonar.ide.client;

import org.sonar.ide.shared.SonarIdeException;
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
  private int serverTrips = 0;

  public SonarClient(String host) {
    super(ConnectorFactory.create(new Host(host)));
    connect();
  }

  private void connect() {
    // TODO check version
    if (false) {
      ServerQuery serverQuery = new ServerQuery();
      Server server = find(serverQuery);
      System.out.println(server.getVersion());
    }
  }

  @Override
  public <MODEL extends Model> MODEL find(Query<MODEL> query) {
    try {
      serverTrips++;
      return super.find(query);
    } catch (ConnectionException e) {
      throw new SonarIdeException(e);
    }
  }

  @Override
  public <MODEL extends Model> Collection<MODEL> findAll(Query<MODEL> query) {
    try {
      serverTrips++;
      return super.findAll(query);
    } catch (ConnectionException e) {
      throw new SonarIdeException(e);
    }
  }

  public int getServerTrips() {
    return serverTrips;
  }
}
