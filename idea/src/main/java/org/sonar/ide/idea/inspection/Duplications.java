package org.sonar.ide.idea.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.sonar.wsclient.Sonar;

import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class Duplications extends AbstractSonarInspectionTool {
  @Override
  protected List<ProblemDescriptor> checkFileBySonar(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly, Sonar sonar) {
    if (isOnTheFly) {
      return null;
    }
    // TODO
    throw new NotImplementedException();
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
    return "Sonar-Duplications"; // TODO
  }

  @Override
  public String getStaticDescription() {
    return "Duplications from Sonar";
  }
}
