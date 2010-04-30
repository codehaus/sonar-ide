/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Sonar-IDE is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar-IDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar-IDE; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.idea;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.sonar.ide.idea.autoupdate.PluginDownloader;
import org.sonar.ide.idea.editor.SonarEditorListener;
import org.sonar.ide.shared.SonarProperties;
import org.sonar.ide.ui.AbstractConfigPanel;
import org.sonar.ide.ui.SonarConfigPanel;

import java.io.File;

/**
 * Per-project plugin component.
 *
 * @author Evgeny Mandrikov
 */
public class IdeaSonarProjectComponent extends AbstractConfigurableComponent {
  private Project project;

  public static IdeaSonarProjectComponent getInstance(Project project) {
    return project.getComponent(IdeaSonarProjectComponent.class);
  }

  public IdeaSonarProjectComponent(Project project) {
    getLog().info("Loaded component for {}", project);
    this.project = project;
    StartupManager.getInstance(project).registerPostStartupActivity(new Runnable() {
      public void run() {
        getLog().info("Start: project initializing");
        initPlugin();
        getLog().info("End: project initialized");
      }
    });
  }

  @Override
  protected AbstractConfigPanel initConfigPanel() {
    SonarConfigPanel configPanel = new SonarConfigPanel();
    configPanel.setProperties(SonarProperties.load(getConfigFilename()));
    return configPanel;
  }

  @Override
  protected void saveConfig(AbstractConfigPanel configPanel) {
    SonarProperties.save(configPanel.getProperties(), getConfigFilename());
  }

  private String getConfigFilename() {
    VirtualFile baseDir = project.getBaseDir();
    getLog().info("Project BaseDir: {}", baseDir);
    if (baseDir == null) {
      // Template settings
      return SonarProperties.getDefaultPath();
    } else {
      // Project settings
      return baseDir.getPath() + File.separator + SonarProperties.FILENAME;
    }
  }

  private void initPlugin() {
    getLog().info("Init plugin");
    // See SONARIDE-12
//    PluginDownloader.checkUpdate();
    EditorFactory.getInstance().addEditorFactoryListener(new SonarEditorListener());
  }

  public SonarProperties getSettings() {
    return new SonarProperties(getConfigFilename());
  }
}
