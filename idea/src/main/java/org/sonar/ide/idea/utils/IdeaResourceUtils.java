package org.sonar.ide.idea.utils;

import com.intellij.psi.PsiJavaFile;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.project.MavenId;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.sonar.ide.shared.AbstractResourceUtils;

/**
 * @author Evgeny Mandrikov
 */
public final class IdeaResourceUtils extends AbstractResourceUtils<PsiJavaFile> {
  private static IdeaResourceUtils instance;

  public static IdeaResourceUtils getInstance() {
    if (instance == null) {
      instance = new IdeaResourceUtils();
    }
    return instance;
  }

  private IdeaResourceUtils() {
  }

  @Override
  public String getFileName(PsiJavaFile file) {
    return StringUtils.substringBeforeLast(file.getName(), ".");
  }

  @NotNull
  @Override
  public String getPackageName(PsiJavaFile file) {
    return file.getPackageName();
  }

  @Nullable
  @Override
  public String getProjectKey(PsiJavaFile file) {
    MavenProject mavenProject = getMavenProject(file);
    return getProjectKey(mavenProject);
  }

  @Nullable
  public String getProjectKey(MavenProject mavenProject) {
    if (mavenProject == null) {
      return null;
    }
    MavenId mavenId = mavenProject.getMavenId();
    return mavenId.getGroupId() + DELIMITER + mavenId.getArtifactId();
  }

  @Nullable
  protected MavenProject getMavenProject(PsiJavaFile file) {
    MavenProjectsManager mavenProjectsManager = MavenProjectsManager.getInstance(file.getProject());
    return mavenProjectsManager.findContainingProject(file.getVirtualFile());
  }
}
