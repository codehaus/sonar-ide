package org.sonar.ide.idea.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import org.jetbrains.idea.maven.project.MavenId;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

/**
 * @author Evgeny Mandrikov
 */
public final class ResourceUtils {
  private static final Logger LOG = Logger.getInstance(ResourceUtils.class.getName());

  /**
   * Default package name for classes without package definition
   */
  public static final String DEFAULT_PACKAGE_NAME = "[default]";

  /**
   * Hide utility-class constructor.
   */
  private ResourceUtils() {
  }

  public static String getResourceKey(PsiElement element) {
    if (element instanceof PsiClass) {
      return getResourceKey((PsiClass) element);
    } else if (element instanceof PsiJavaFile) {
      return getResourceKey((PsiJavaFile) element);
    } else {
      return null;
    }
  }

  public static String getResourceKey(PsiClass aClass) {
    String name = aClass.getName();
    String parentKey = getResourceKey(aClass.getParent());
    return new StringBuilder().append(parentKey).append('.').append(name).toString();
  }

  public static String getResourceKey(PsiJavaFile file) {
    String projectKey = getProjectKey(file);
    String packageKey = file.getPackageName();
    if (packageKey.length() == 0) {
      packageKey = DEFAULT_PACKAGE_NAME;
    }
    return new StringBuilder().append(projectKey).append(':').append(packageKey).toString();
  }

  public static String getProjectKey(PsiJavaFile file) {
    MavenProjectsManager mavenProjectsManager = MavenProjectsManager.getInstance(file.getProject());
    MavenProject mavenProject = mavenProjectsManager.findContainingProject(file.getVirtualFile());
    MavenId mavenId = mavenProject.getMavenId();
    String projectKey = mavenId.getGroupId() + ":" + mavenId.getArtifactId();
    LOG.debug(projectKey);
    return projectKey;
  }
}
