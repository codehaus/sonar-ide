package org.sonar.ide.idea;

import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.inspection.Violations;

/**
 * Per-application plugin component.
 *
 * @author Evgeny Mandrikov
 */
public class IdeaSonarApplicationComponent implements ApplicationComponent, InspectionToolProvider {
  private static final Logger LOG = LoggerFactory.getLogger(IdeaSonarApplicationComponent.class);

  @NotNull
  @Override
  public String getComponentName() {
    return getClass().getSimpleName();
  }

  @Override
  public void initComponent() {
    LOG.info("Init component");
  }

  @Override
  public void disposeComponent() {
    LOG.info("Dispose component");
  }

  @Override
  public Class[] getInspectionClasses() {
    return new Class[]{
        Violations.class,
//        Duplications.class,
//        Dependencies.class
    };
  }
}
