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
import org.mockito.Mockito;
import org.sonar.ide.shared.AbstractResourceUtilsTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Evgeny Mandrikov
 */
public class IdeaResourceUtilsTest extends AbstractResourceUtilsTest<PsiFile> {
  @Override
  protected PsiFile newFileModel(boolean java, String projectKey, String packageOrDirectory, String fileName) {
    if (java) {
      PsiJavaFile psiJavaFile = mock(PsiJavaFile.class);
      when(psiJavaFile.getName()).thenReturn(fileName);
      when(psiJavaFile.getPackageName()).thenReturn(packageOrDirectory);
      return psiJavaFile;
    } else {
      PsiFile psiFile = mock(PsiFile.class);
      when(psiFile.getName()).thenReturn(fileName);
      return psiFile;
    }
  }

  @Override
  protected IdeaResourceUtils newUtils(boolean java, String projectKey, String packageOrDirectory, String fileName) {
    IdeaResourceUtils utils = Mockito.spy(IdeaResourceUtils.getInstance());
    Mockito.doReturn(projectKey).when(utils).getProjectKey(Mockito.<PsiFile>any());
    Mockito.doReturn(packageOrDirectory).when(utils).getDirectoryPath(Mockito.<PsiFile>any());
    return utils;
  }
}
