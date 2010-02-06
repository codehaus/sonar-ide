package org.sonar.ide.idea.utils.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.idea.maven.utils.actions.MavenActionUtil;

/**
 * @author Evgeny Mandrikov
 */
public class SonarActionGroup extends DefaultActionGroup {
  @Override
  public void update(AnActionEvent event) {
    super.update(event);
    boolean available = isAvailable(event);
    Presentation p = event.getPresentation();
    p.setEnabled(available);
    p.setVisible(available);
  }

  protected boolean isAvailable(AnActionEvent event) {
    return MavenActionUtil.getProject(event) != null && !MavenActionUtil.getMavenProjects(event).isEmpty();
  }
}
