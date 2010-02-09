package org.sonar.ide.idea.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.utils.SonarUtils;
import org.sonar.wsclient.Sonar;

import javax.swing.*;
import java.util.List;

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

  @Nullable
  @Override
  public final ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
    getLog().debug("Running " + (isOnTheFly ? "on the fly" : "offline") + " inspection for " + file);
    Sonar sonar = SonarUtils.getSonar(file.getProject());
    List<ProblemDescriptor> problems = checkFileBySonar(file, manager, isOnTheFly, sonar);
    if (problems == null) {
      return null;
    }
    return problems.toArray(new ProblemDescriptor[problems.size()]);
  }

  @Nullable
  protected abstract List<ProblemDescriptor> checkFileBySonar(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly, Sonar sonar);

  @NotNull
  protected TextRange getTextRange(@NotNull Document document, int line) {
    int lineStartOffset = document.getLineStartOffset(line);
    int lineEndOffset = document.getLineEndOffset(line);
    return new TextRange(lineStartOffset, lineEndOffset);
  }
}
