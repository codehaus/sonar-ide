package org.sonar.ide.idea.maven;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

/**
 * @author Evgeny Mandrikov
 */
public final class SonarMavenUtils {
  private static final String MAVEN_PLUGIN_ID = "org.jetbrains.idea.maven";

  public static boolean isMavenPluginInstalled() {
    return PluginManager.isPluginInstalled(PluginId.getId(MAVEN_PLUGIN_ID));
  }

  public static boolean isMavenModule(Module module) {
    if (!isMavenPluginInstalled()) {
      return false;
    }
    Project project = module.getProject();
    VirtualFile moduleFile = module.getModuleFile();
    if (moduleFile == null) {
      return false;
    }
    MavenProjectsManager mavenProjectsManager = MavenProjectsManager.getInstance(project);
    MavenProject mavenProject = mavenProjectsManager.findContainingProject(moduleFile);
    return mavenProject != null;
  }

  /**
   * Hide utility-class constructor.
   */
  private SonarMavenUtils() {
  }
}
