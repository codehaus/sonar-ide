package org.sonar.ide.idea;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.sonar.ide.ui.AbstractConfigPanel;

import javax.swing.*;
import java.util.Properties;

/**
 * Per-module plugin component.
 *
 * @author Evgeny Mandrikov
 */
public class IdeaSonarModuleComponent extends AbstractConfigurableComponent implements ModuleComponent {
  private Module module;

  public IdeaSonarModuleComponent(final Module module) {
    getLog().info("Loaded component for {}", module);
    this.module = module;
  }

  @Override
  protected AbstractConfigPanel initConfigPanel() {
    Project project = module.getProject();
    VirtualFile moduleFile = module.getModuleFile();
    getLog().info("Project: " + project);
    getLog().info("Module File: " + moduleFile);
    boolean isMavenModule = false;
    if (moduleFile != null) {
      MavenProjectsManager mavenProjectsManager = MavenProjectsManager.getInstance(project);
      MavenProject mavenProject = mavenProjectsManager.findContainingProject(moduleFile);
      isMavenModule = mavenProject != null;
    }
    // TODO
    return new MyConfigPanel(isMavenModule ?
        "This is a Maven module, so Sonar will be enabled for this module." :
        "Currently non-Maven modules not supported, so Sonar will be disabled for this module."
    );
  }

  class MyConfigPanel extends AbstractConfigPanel {
    MyConfigPanel(String text) {
      add(new JLabel(text));
    }

    @Override
    public boolean isModified() {
      return false;
    }

    @Override
    public Properties getProperties() {
      return null;
    }
  }
}
