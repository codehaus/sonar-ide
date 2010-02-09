package org.sonar.ide.idea.utils;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.project.MavenId;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.sonar.ide.shared.AbstractResourceUtils;

/**
 * @author Evgeny Mandrikov
 */
public final class IdeaResourceUtils extends AbstractResourceUtils<PsiFile> {
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
  protected boolean isJavaFile(PsiFile file) {
    return file instanceof PsiJavaFile;
  }

  @Override
  public String getFileName(PsiFile file) {
    return isJavaFile(file) ?
        StringUtils.substringBeforeLast(file.getName(), ".") :
        file.getName();
  }

  @Nullable
  @Override
  public String getPackageName(PsiFile file) {
    if (isJavaFile(file)) {
      PsiJavaFile psiJavaFile = (PsiJavaFile) file;
      return psiJavaFile.getPackageName();
    }
    return null;
  }

  @Nullable
  @Override
  protected String getDirectoryPath(PsiFile file) {
    // TODO implement me
    throw new NotImplementedException("Currently only java files supported");
  }

  @Nullable
  @Override
  public String getProjectKey(PsiFile file) {
    MavenProject mavenProject = getMavenProject(file);
    return getProjectKey(mavenProject);
  }

  @Nullable
  public String getProjectKey(MavenProject mavenProject) {
    if (mavenProject == null) {
      return null;
    }
    MavenId mavenId = mavenProject.getMavenId();
    return getProjectKey(mavenId.getGroupId(), mavenId.getArtifactId());
  }

  @Nullable
  protected MavenProject getMavenProject(PsiFile file) {
    MavenProjectsManager mavenProjectsManager = MavenProjectsManager.getInstance(file.getProject());
    return mavenProjectsManager.findContainingProject(file.getVirtualFile());
  }
}
