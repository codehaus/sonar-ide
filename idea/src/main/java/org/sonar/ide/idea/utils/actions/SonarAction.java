package org.sonar.ide.idea.utils.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.idea.maven.utils.actions.MavenActionUtil;

/**
 * @author Evgeny Mandrikov
 */
public abstract class SonarAction extends AnAction implements DumbAware {
  @Override
  public void update(AnActionEvent event) {
    super.update(event);
    Presentation p = event.getPresentation();
    p.setEnabled(isAvailable(event));
    p.setVisible(isVisible(event));
  }

  protected boolean isAvailable(AnActionEvent event) {
    return MavenActionUtil.getProject(event) != null;
  }

  protected boolean isVisible(AnActionEvent event) {
    return true;
  }
}
