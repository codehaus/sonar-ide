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

package org.sonar.ide.idea.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * @author Evgeny Mandrikov
 */
public abstract class AbstractSonarInspectionTool extends LocalInspectionTool {
  protected Logger getLog() {
    return LoggerFactory.getLogger(getClass());
  }

  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return "Sonar";
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Override
  public JComponent createOptionsPanel() {
    getLog().debug("Create options panel");
    return super.createOptionsPanel();
  }

  @Override
  public PsiNamedElement getProblemElement(PsiElement psiElement) {
    PsiNamedElement result = super.getProblemElement(psiElement);
    getLog().debug("Get problem element for " + psiElement + " with result " + result);
    return result;
  }

  @Override
  public void inspectionStarted(LocalInspectionToolSession session) {
    getLog().debug("Inspection started");
    super.inspectionStarted(session);
  }

  @Override
  public void inspectionFinished(LocalInspectionToolSession session) {
    getLog().debug("Inspection finished");
    super.inspectionFinished(session);
  }

  @NotNull
  protected TextRange getTextRange(@NotNull Document document, int line) {
    int lineStartOffset = document.getLineStartOffset(line);
    int lineEndOffset = document.getLineEndOffset(line);
    return new TextRange(lineStartOffset, lineEndOffset);
  }
}
