package org.sonar.ide.netbeans;

import org.openide.modules.ModuleInstall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.netbeans.editor.AnnotationContainer;

/**
 * Manages a module's lifecycle.
 *
 * @author Evgeny Mandrikov
 */
public class SonarModule extends ModuleInstall {
  private static final Logger LOG = LoggerFactory.getLogger(SonarModule.class);

  @Override
  public void restored() {
    LOG.info("Sonar plugin restored");
  }

  @Override
  public void uninstalled() {
    AnnotationContainer.reset();
    LOG.info("Sonar plugin unistalled");
  }
}
