/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.idea.utils;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.idea.IdeaSonarModuleComponent;
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
    IdeaSonarModuleComponent sonarModule = IdeaSonarModuleComponent.getInstance(file);
    if (sonarModule == null) {
      return null;
    }
    return getProjectKey(sonarModule.getGroupId(), sonarModule.getArtifactId(), sonarModule.getBranch());
  }
}
