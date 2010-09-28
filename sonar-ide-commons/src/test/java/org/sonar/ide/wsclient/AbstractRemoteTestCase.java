package org.sonar.ide.wsclient;

import org.sonar.ide.test.SonarIdeTestCase;

public abstract class AbstractRemoteTestCase extends SonarIdeTestCase {

  /**
   * Convenience method.
   * 
   * @return {@link RemoteSonar} for {@link #getTestServer()}
   */
  protected RemoteSonar getRemoteSonar() throws Exception {
    return new RemoteSonar(getTestServer().getHost());
  }

}
