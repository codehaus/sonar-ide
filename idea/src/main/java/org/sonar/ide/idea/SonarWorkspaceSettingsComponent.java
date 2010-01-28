package org.sonar.ide.idea;

import com.intellij.openapi.components.SettingsSavingComponent;
import com.intellij.openapi.project.Project;
import org.sonar.ide.shared.SonarProperties;

/**
 * @author Evgeny Mandrikov
 */
public class SonarWorkspaceSettingsComponent implements SettingsSavingComponent {
  private SonarProperties settings;

  public static SonarWorkspaceSettingsComponent getInstance(Project project) {
    return project.getComponent(SonarWorkspaceSettingsComponent.class);
  }

  public SonarWorkspaceSettingsComponent() {
    load();
  }

  public SonarProperties getSettings() {
    return settings;
  }

  private void load() {
    settings = new SonarProperties(SonarProperties.getDefaultPath());
  }

  public void save() {
  }
}
