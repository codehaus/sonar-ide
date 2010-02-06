package org.sonar.ide.idea.utils.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.idea.maven.utils.actions.MavenActionUtil;

/**
 * @author Evgeny Mandrikov
 */
public abstract class SonarToggleAction extends ToggleAction implements DumbAware {
  @Override
  public void update(AnActionEvent event) {
    super.update(event);
    event.getPresentation().setEnabled(isAvailable(event));
  }

  protected boolean isAvailable(AnActionEvent event) {
    return MavenActionUtil.getProject(event) != null;
  }

  public final boolean isSelected(AnActionEvent event) {
    return isAvailable(event) && doIsSelected(event);
  }

  protected abstract boolean doIsSelected(AnActionEvent event);
}
