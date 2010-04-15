package org.sonar.ide.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.ConnectionException;
import org.sonar.wsclient.connectors.ConnectorFactory;
import org.sonar.wsclient.services.Model;
import org.sonar.wsclient.services.Query;
import org.sonar.wsclient.services.Server;
import org.sonar.wsclient.services.ServerQuery;

import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class SonarClient extends Sonar {
    private static final Logger LOG = LoggerFactory.getLogger(SonarClient.class);

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
            LOG.info("Connect");
            ServerQuery serverQuery = new ServerQuery();
            Server server = find(serverQuery);
            available = checkVersion(server);
            LOG.info(available ? "Connected to " + server.getId() + "(" + server.getVersion() + ")" : "Unable to connect");
        } catch (ConnectionException e) {
            available = false;
            LOG.error("Unable to connect", e);
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
        LOG.info("find : {}", query.getUrl());
        MODEL model = super.find(query);
        LOG.info(model.toString());
        return model;
    }

    @Override
    public <MODEL extends Model> List<MODEL> findAll(Query<MODEL> query) {
        serverTrips++;
        LOG.info("find : {}", query.getUrl());
        List<MODEL> result = super.findAll(query);
        LOG.info("Retrieved {} elements.", result.size());
        return result;
    }

    public int getServerTrips() {
        return serverTrips;
    }

    public boolean isAvailable() {
        return available;
    }
}