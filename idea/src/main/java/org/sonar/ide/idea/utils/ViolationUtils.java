package org.sonar.ide.idea.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Violation;
import org.sonar.wsclient.services.ViolationQuery;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Evgeny Mandrikov
 */
public final class ViolationUtils {
  static final Logger LOG = Logger.getInstance(ViolationUtils.class.getName());

  /**
   * Hide utility-class constructor.
   */
  private ViolationUtils() {
  }

  public static Collection<Violation> getViolations(PsiFile file) {
    ArrayList<Violation> violations = new ArrayList<Violation>();
    if (file instanceof PsiJavaFile) {
      PsiClass[] classes = ((PsiJavaFile) file).getClasses();
      for (PsiClass psiClass : classes) {
        violations.addAll(getViolations(psiClass));
      }
    }
    return violations;
  }

  public static Collection<Violation> getViolations(PsiClass aClass) {
    Sonar sonar = SonarUtils.getSonar(aClass.getProject());
    return getViolations(sonar, ResourceUtils.getResourceKey(aClass));
  }

  public static Collection<Violation> getViolations(Sonar sonar, String resourceKey) {
    LOG.debug("Loading violations for " + resourceKey);
    ViolationQuery query = ViolationQuery.createForResource(resourceKey);
    return sonar.findAll(query);
  }
}
