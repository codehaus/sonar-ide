package org.sonar.ide.idea.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.idea.SonarWorkspaceSettingsComponent;
import org.sonar.ide.idea.utils.InspectionUtils;
import org.sonar.ide.idea.utils.ViolationUtils;

import javax.swing.*;

/**
 * @author Evgeny Mandrikov
 */
public class SonarInspection extends LocalInspectionTool {
  private static final Logger LOG = Logger.getInstance(SonarInspection.class.getName());

  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return "Sonar";
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return "Sonar Analysis"; // TODO
  }

  @NotNull
  @Override
  public String getShortName() {
    return "Sonar-IDEA"; // TODO
  }

  @Override
  public String getStaticDescription() {
    return "Static description"; // TODO
  }

  @Nullable
  @Override
  public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
    if (isOnTheFly && !SonarWorkspaceSettingsComponent.getInstance(file.getProject()).getSettings().isOnTheFly()) {
      return null;
    }
    LOG.debug("Running " + (isOnTheFly ? "on the fly" : "offline") + " inspection for " + file);
    return InspectionUtils.buildProblemDescriptors(
        ViolationUtils.getViolations(file),
        manager,
        file,
        isOnTheFly
    );
  }

  @Override
  public JComponent createOptionsPanel() {
    LOG.debug("Create options panel");
    return super.createOptionsPanel();
  }

  @Override
  public PsiNamedElement getProblemElement(PsiElement psiElement) {
    PsiNamedElement result = super.getProblemElement(psiElement);
    LOG.debug("Get problem element for " + psiElement + " with result " + result);
    return result;
  }

  @Override
  public void inspectionStarted(LocalInspectionToolSession session) {
    LOG.debug("Inspection started");
    super.inspectionStarted(session);
  }

  @Override
  public void inspectionFinished(LocalInspectionToolSession session) {
    LOG.debug("Inspection finished");
    super.inspectionFinished(session);
  }
}
