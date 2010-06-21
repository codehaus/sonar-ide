package org.sonar.ide.wsclient;

import org.sonar.ide.api.SourceCode;

public final class RemoteSonarUtils {

  public static String getUrl(SourceCode sourceCode) {
    RemoteSourceCode remoteSourceCode = (RemoteSourceCode) sourceCode;
    String host = remoteSourceCode.getRemoteSonarIndex().getServer().getHost();
    return host + "/resource/index/" + remoteSourceCode.getKey();
  }

  /**
   * Hide utility-class constructor.
   */
  private RemoteSonarUtils() {
  }

}
