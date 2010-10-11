package org.sonar.ide.client;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.connectors.ConnectionException;
import org.sonar.wsclient.connectors.Connector;
import org.sonar.wsclient.connectors.HttpClient3Connector;
import org.sonar.wsclient.services.AbstractQuery;
import org.sonar.wsclient.services.CreateQuery;
import org.sonar.wsclient.services.DeleteQuery;
import org.sonar.wsclient.services.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.URI;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ExtendedHttpClient3Connector extends Connector {
  public static final int TIMEOUT_MS = 30000;
  private static final int MAX_TOTAL_CONNECTIONS = 40;
  private static final int MAX_HOST_CONNECTIONS = 4;

  private final Host server;
  private HttpClient httpClient;

  public ExtendedHttpClient3Connector(Host server) {
    this.server = server;
    createClient();
  }

  /**
   * Workaround for <a href="http://jira.codehaus.org/browse/SONAR-1715">SONAR-1715</a> and
   * for <a href="http://jira.codehaus.org/browse/SONAR-1586">SONAR-1586</a>.
   * <p>
   * TODO Godin: I suppose that call of method {@link HttpClient3Connector#configureCredentials()} should be added to constructor
   * {@link HttpClient3Connector#HttpClient3Connector(Host, HttpClient)}
   * </p>
   * 
   * @see org.sonar.wsclient.connectors.HttpClient3Connector#createClient()
   * @see org.sonar.wsclient.connectors.HttpClient3Connector#configureCredentials()
   */
  private void createClient() {
    // createClient
    final HttpConnectionManagerParams params = new HttpConnectionManagerParams();
    params.setConnectionTimeout(TIMEOUT_MS);
    params.setSoTimeout(TIMEOUT_MS);
    params.setDefaultMaxConnectionsPerHost(MAX_HOST_CONNECTIONS);
    params.setMaxTotalConnections(MAX_TOTAL_CONNECTIONS);
    final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
    connectionManager.setParams(params);
    httpClient = new HttpClient(connectionManager);

    // configureCredentials
    if (StringUtils.isNotEmpty(server.getUsername())) {
      httpClient.getParams().setAuthenticationPreemptive(true);
      Credentials defaultcreds = new UsernamePasswordCredentials(server.getUsername(), server.getPassword());
      httpClient.getState().setCredentials(AuthScope.ANY, defaultcreds);
    }

    // TODO Godin: ugly hack to use ProxySelector with commons-httpclient 3.1
    try {
      List<Proxy> list = ProxySelector.getDefault().select(new URI(server.getHost()));
      for (Proxy proxy : list) {
        if (proxy.type() == Proxy.Type.HTTP) {
          // Proxy
          InetSocketAddress addr = (InetSocketAddress) proxy.address();
          httpClient.getHostConfiguration().setProxy(addr.getHostName(), addr.getPort());
          // Proxy auth
          InetAddress proxyInetAddress = InetAddress.getByName(addr.getHostName());
          PasswordAuthentication auth = Authenticator.requestPasswordAuthentication(proxyInetAddress, addr.getPort(), null, null, null);
          if (auth != null) {
            Credentials defaultcreds = new UsernamePasswordCredentials(auth.getUserName(), new String(auth.getPassword()));
            httpClient.getState().setProxyCredentials(AuthScope.ANY, defaultcreds);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ExtendedHttpClient3Connector(Host server, HttpClient httpClient) {
    this.httpClient = httpClient;
    this.server = server;
  }

  public String execute(Query query) {
    return executeRequest(query, newGetRequest(query));
  }

  public String execute(CreateQuery query) {
    return executeRequest(query, newPostRequest(query));
  }

  public String execute(DeleteQuery query) {
    return executeRequest(query, newDeleteRequest(query));
  }

  private String executeRequest(AbstractQuery query, HttpMethodBase method) {
    String json = null;
    try {
      httpClient.executeMethod(method);
      if (method.getStatusCode() == HttpStatus.SC_OK) {
        json = getResponseBodyAsString(query, method);

      } else if (method.getStatusCode() != HttpStatus.SC_NOT_FOUND) {
        throw new ConnectionException("HTTP error: " + method.getStatusCode() + ", msg: " + method.getStatusText() + ", query: " + getQueryUrl(query));
      }

    } catch (IOException e) {
      throw new ConnectionException(errorMessage(query, e), e);

    } finally {
      if (method != null) {
        method.releaseConnection();
      }
    }
    return json;
  }

  private String getQueryUrl(AbstractQuery query) {
    return server.getHost() + query.getUrl();
  }

  private String errorMessage(AbstractQuery query, Throwable cause) {
    String message = "Can not read response for query " + getQueryUrl(query);
    return cause.getMessage() == null ? message : message + " :\n" + cause.getMessage();
  }

  private HttpMethodBase newGetRequest(Query query) {
    try {
      String url = server.getHost() + URIUtil.encodeQuery(query.getUrl());
      HttpMethodBase method = new GetMethod(url);
      method.setRequestHeader("Accept", "application/json");
      return method;

    } catch (URIException e) {
      throw new ConnectionException(errorMessage(query, e), e);
    }
  }

  private HttpMethodBase newPostRequest(CreateQuery query) {
    try {
      String url = server.getHost() + URIUtil.encodeQuery(query.getUrl());
      HttpMethodBase method = new PostMethod(url);
      method.setRequestHeader("Accept", "application/json");
      return method;

    } catch (URIException e) {
      throw new ConnectionException(errorMessage(query, e), e);
    }
  }

  private HttpMethodBase newDeleteRequest(DeleteQuery query) {
    try {
      String url = server.getHost() + URIUtil.encodeQuery(query.getUrl());
      HttpMethodBase method = new DeleteMethod(url);
      method.setRequestHeader("Accept", "application/json");
      return method;

    } catch (URIException e) {
      throw new ConnectionException(errorMessage(query, e), e);
    }
  }

  private String getResponseBodyAsString(AbstractQuery query, HttpMethod method) {
    BufferedReader reader = null;
    try {
      final InputStream inputStream = method.getResponseBodyAsStream();
      reader = new BufferedReader(new InputStreamReader(inputStream));
      final StringBuilder sb = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        sb.append(line).append("\n");
      }
      return sb.toString();

    } catch (IOException e) {
      throw new ConnectionException(errorMessage(query, e), e);

    } finally {
      if (reader != null) {
        try {
          reader.close();

        } catch (Exception e) {
          // TODO
        }
      }
    }
  }
}
