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

package org.sonar.ide.idea;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.SelectInContext;
import com.intellij.ide.SelectInTarget;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.idea.utils.IdeaResourceUtils;
import org.sonar.ide.idea.utils.SonarUtils;
import org.sonar.ide.shared.SonarUrlUtils;

/**
 * @author Evgeny Mandrikov
 * @see org.sonar.ide.idea.actions.GoToDashboardAction
 */
public class SelectInSonarTarget implements SelectInTarget {
  @Override
  public boolean canSelect(SelectInContext context) {
    Object selector = context.getSelectorInFile();
    Project project = context.getProject();
    return getSonarUrl(selector, project) != null;
  }

  @Override
  public void selectIn(SelectInContext context, boolean requestFocus) {
    Object selector = context.getSelectorInFile();
    Project project = context.getProject();
    String url = getSonarUrl(selector, project);
    if (url != null) {
      BrowserUtil.launchBrowser(url);
    }
  }

  @Nullable
  private String getSonarUrl(Object selector, Project project) {
    if (selector instanceof PsiElement) {
      PsiElement psiElement = (PsiElement) selector;
      PsiFile psiFile = psiElement.getContainingFile();
      String resourceKey = IdeaResourceUtils.getInstance().getFileKey(psiFile);
      return SonarUrlUtils.getDashboard(
          SonarUtils.getSonarSettings(project).getServer().getHost(),
          resourceKey
      );
    }
    return null;
  }

  @Override
  public float getWeight() {
    return Float.MAX_VALUE;
  }

  @Nullable
  @Override
  public String getToolWindowId() {
    return null;
  }

  @Nullable
  @Override
  public String getMinorViewId() {
    return null;
  }

  @Override
  public String toString() {
    return "Sonar";
  }
}
