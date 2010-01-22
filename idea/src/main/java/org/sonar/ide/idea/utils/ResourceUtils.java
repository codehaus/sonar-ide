package org.sonar.ide.idea.utils;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import org.sonar.ide.idea.SonarWorkspaceSettingsComponent;
import org.sonar.ide.idea.config.SonarSettings;

/**
 * @author Evgeny Mandrikov
 */
public final class ResourceUtils {
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
    String projectKey = getResourceKey(file.getProject());
    String packageKey = file.getPackageName();
    if (packageKey.length() == 0) {
      packageKey = DEFAULT_PACKAGE_NAME;
    }
    return new StringBuilder().append(projectKey).append(':').append(packageKey).toString();
  }

  public static String getResourceKey(Project project) {
    SonarSettings sonarSettings = SonarWorkspaceSettingsComponent.getInstance(project).getSettings();
    return sonarSettings.getProjectKey();
  }
}
