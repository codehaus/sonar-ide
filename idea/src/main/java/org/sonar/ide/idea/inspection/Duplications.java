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

package org.sonar.ide.idea.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.idea.utils.SonarUtils;
import org.sonar.ide.shared.duplications.Duplication;
import org.sonar.ide.shared.duplications.DuplicationUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class Duplications extends AbstractSonarInspectionTool {
  @Nullable
  @Override
  public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
    getLog().debug("Running " + (isOnTheFly ? "on the fly" : "offline") + " inspection for " + file);

    List<ProblemDescriptor> problems = buildProblemDescriptors(
        SonarUtils.getIdeaSonar(file.getProject()).search(file).getDuplications(),
        manager,
        file,
        isOnTheFly
    );

    if (problems == null) {
      return null;
    }
    return problems.toArray(new ProblemDescriptor[problems.size()]);
  }

  @Nullable
  private List<ProblemDescriptor> buildProblemDescriptors(
      @Nullable Collection<Duplication> duplications,
      @NotNull InspectionManager manager,
      PsiElement element,
      boolean isOnTheFly
  ) {
    if (duplications == null) {
      return null;
    }

    Project project = element.getProject();
    PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
    Document document = documentManager.getDocument(element.getContainingFile());
    if (document == null) {
      return null;
    }

    List<ProblemDescriptor> problems = new ArrayList<ProblemDescriptor>();
    for (Duplication duplication : duplications) {
      int line = duplication.getStart() - 1;
      problems.add(manager.createProblemDescriptor(
          element,
          getTextRange(document, line),
          DuplicationUtils.getDescription(duplication),
          ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
          isOnTheFly,
          LocalQuickFix.EMPTY_ARRAY
      ));
    }
    return problems;
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return "Duplications";
  }

  @NotNull
  @Override
  public String getShortName() {
    return "Sonar.Duplications"; // TODO
  }

  @Override
  public String getStaticDescription() {
    return "Duplications from Sonar";
  }
}
