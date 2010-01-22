package org.sonar.ide.idea;

import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.sonar.ide.idea.inspection.SonarInspection;

/**
 * @author Evgeny Mandrikov
 */
public class SonarInspectionProvider implements InspectionToolProvider, ApplicationComponent {
  public SonarInspectionProvider() {
  }

  public void initComponent() {
  }

  public void disposeComponent() {
  }

  @NonNls
  @NotNull
  public String getComponentName() {
    return getClass().getSimpleName();
  }

  public Class[] getInspectionClasses() {
    return new Class[]{SonarInspection.class};
  }
}
