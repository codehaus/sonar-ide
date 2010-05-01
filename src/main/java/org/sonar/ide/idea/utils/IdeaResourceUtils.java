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

package org.sonar.ide.idea.utils;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.project.MavenId;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.sonar.ide.shared.AbstractResourceUtils;

/**
 * @author Evgeny Mandrikov
 */
public class IdeaResourceUtils extends AbstractResourceUtils<PsiFile> {
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
//    throw new NotImplementedException("Currently only java files supported");
    return null;
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
