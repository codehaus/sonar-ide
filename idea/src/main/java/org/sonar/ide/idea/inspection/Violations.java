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
import org.sonar.ide.idea.utils.IdeaResourceUtils;
import org.sonar.ide.shared.ViolationUtils;
import org.sonar.ide.shared.ViolationsLoader;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Violation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class Violations extends AbstractSonarInspectionTool {
  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return "Violations";
  }

  @NotNull
  @Override
  public String getShortName() {
    return "Sonar-Violations"; // TODO
  }

  @Override
  public String getStaticDescription() {
    return "Violations from Sonar (Checkstyle, PMD, Findbugs, ...)";
  }

  @Nullable
  @Override
  public List<ProblemDescriptor> checkFileBySonar(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly, Sonar sonar) {
    if (isOnTheFly) {
      return null;
    }

    String text = file.getText();
    Collection<Violation> violations = ViolationsLoader.getViolations(
        sonar,
        IdeaResourceUtils.getInstance().getFileKey(file),
        text
    );

    return buildProblemDescriptors(
        violations,
        manager,
        file,
        isOnTheFly
    );
  }

  @Nullable
  private List<ProblemDescriptor> buildProblemDescriptors(
      @Nullable Collection<Violation> violations,
      @NotNull InspectionManager manager,
      PsiElement element,
      boolean isOnTheFly
  ) {
    if (violations == null) {
      return null;
    }

    Project project = element.getProject();
    PsiDocumentManager documentManager = PsiDocumentManager.getInstance(project);
    Document document = documentManager.getDocument(element.getContainingFile());
    if (document == null) {
      return null;
    }

    List<ProblemDescriptor> problems = new ArrayList<ProblemDescriptor>();
    for (Violation violation : violations) {
      int line = violation.getLine() - 1;
      problems.add(manager.createProblemDescriptor(
          element,
          getTextRange(document, line),
          ViolationUtils.getDescription(violation),
          ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
          isOnTheFly,
          LocalQuickFix.EMPTY_ARRAY
      ));
    }
    return problems;
  }
}
