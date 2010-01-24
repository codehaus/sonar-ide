package org.sonar.ide.idea;

import com.intellij.openapi.components.SettingsSavingComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.idea.config.SonarSettings;

import java.io.File;

/**
 * @author Evgeny Mandrikov
 */
public class SonarWorkspaceSettingsComponent implements SettingsSavingComponent {
  private static final Logger LOG = Logger.getInstance(SonarWorkspaceSettingsComponent.class.getName());

  private SonarSettings settings;
  private Project project;

  public static SonarWorkspaceSettingsComponent getInstance(Project project) {
    return project.getComponent(SonarWorkspaceSettingsComponent.class);
  }

  public SonarWorkspaceSettingsComponent(Project project) {
    this.project = project;
    load();
  }

  public SonarSettings getSettings() {
    return settings;
  }

  @Nullable
  private String getCfgFilePath() {
    final VirtualFile basedir = project.getBaseDir();
    if (basedir == null) {
      return null;
    }
    String cfgFilePath = basedir.getPath() + File.separator + "sonar-ide.properties";
    LOG.debug("Config: " + cfgFilePath);
    return cfgFilePath;
  }

  private void load() {
    settings = SonarSettings.load(getCfgFilePath());
  }

  public void save() {
    settings.save(getCfgFilePath());
  }
}
