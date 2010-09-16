package org.sonar.ide.wsclient;

import org.sonar.wsclient.services.ViolationQuery;

/**
 * Workaround for SONAR-1793: Wrong URL construction in ViolationQuery, when depth parameter used.
 * 
 * @deprecated Should be removed after release of sonar-ws-client 2.3
 */
@Deprecated
public class ProperViolationQuery extends ViolationQuery {
  public ProperViolationQuery(String resourceKeyOrId) {
    super(resourceKeyOrId);
  }

  @Override
  public String getUrl() {
    String url = super.getUrl();
    if (getDepth() != 0) {
      url += "depth=" + getDepth();
    }
    return url;
  }
}
