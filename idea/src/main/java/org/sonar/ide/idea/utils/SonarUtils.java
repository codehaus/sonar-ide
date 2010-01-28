package org.sonar.ide.idea.utils;

import com.intellij.openapi.project.Project;
import org.sonar.ide.idea.SonarWorkspaceSettingsComponent;
import org.sonar.ide.shared.SonarProperties;
import org.sonar.wsclient.Server;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.Violation;
import org.sonar.wsclient.services.ViolationQuery;

import java.util.Collection;

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

  public static SonarProperties getSonarSettings(Project project) {
    return SonarWorkspaceSettingsComponent.getInstance(project).getSettings();
  }

  public static Sonar getSonar(Project project) {
    return new Sonar(new HttpClient4Connector(getSonarSettings(project).getServer()));
  }

  public static void main(String[] args) {
    Sonar sonar = new Sonar(new HttpClient4Connector(new Server("http://localhost:9000")));
    ViolationQuery query = new ViolationQuery("org.codehaus.sonar-ide:test-project:[default].ClassOnDefaultPackage");
    Collection<Violation> violations = sonar.findAll(query);
  }
}
