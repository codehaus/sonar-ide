package org.sonar.ide.idea;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Per-application plugin component.
 *
 * @author Evgeny Mandrikov
 */
public class IdeaSonarApplicationComponent implements ApplicationComponent {
  private static final Logger LOG = LoggerFactory.getLogger(IdeaSonarApplicationComponent.class);

  @NotNull
  public String getComponentName() {
    return getClass().getSimpleName();
  }

  public void initComponent() {
    LOG.info("Init component");
    // TODO
  }

  public void disposeComponent() {
    LOG.info("Dispose component");
    // TODO
  }
}
