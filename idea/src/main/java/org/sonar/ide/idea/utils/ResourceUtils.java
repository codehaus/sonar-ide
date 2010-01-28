package org.sonar.ide.idea.utils;

import com.intellij.psi.PsiJavaFile;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.idea.maven.project.MavenId;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.sonar.ide.shared.AbstractResourceUtils;

/**
 * @author Evgeny Mandrikov
 */
public final class ResourceUtils extends AbstractResourceUtils<PsiJavaFile> {
  @Override
  public String getFileName(PsiJavaFile file) {
    return StringUtils.substringBeforeLast(file.getName(), ".");
  }

  @Override
  public String getPackageName(PsiJavaFile file) {
    return file.getPackageName();
  }

  @Override
  public String getProjectKey(PsiJavaFile file) {
    MavenProjectsManager mavenProjectsManager = MavenProjectsManager.getInstance(file.getProject());
    MavenProject mavenProject = mavenProjectsManager.findContainingProject(file.getVirtualFile());
    MavenId mavenId = mavenProject.getMavenId();
    return mavenId.getGroupId() + DELIMITER + mavenId.getArtifactId();
  }
}
