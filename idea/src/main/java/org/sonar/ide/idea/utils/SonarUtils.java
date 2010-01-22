package org.sonar.ide.idea.utils;

import com.intellij.openapi.project.Project;
import org.sonar.ide.idea.SonarWorkspaceSettingsComponent;
import org.sonar.ide.idea.config.SonarSettings;
import org.sonar.wsclient.Server;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;

/**
 * @author Evgeny Mandrikov
 */
public final class SonarUtils {
  /**
   * Hide utility-class constructor.
   */
  private SonarUtils() {
  }

  public static Sonar getSonar(Server server) {
    return new Sonar(new HttpClient4Connector(server));
//    return new Sonar(new HttpClient3Connector(server));
  }

  public static SonarSettings getSonarSettings(Project project) {
    return SonarWorkspaceSettingsComponent.getInstance(project).getSettings();
  }

  public static Sonar getSonar(Project project) {
    return new Sonar(new HttpClient4Connector(getSonarSettings(project).getServer()));
  }
}
