package org.sonar.ide.idea.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.sonar.ide.idea.utils.actions.SonarAction;
import org.sonar.ide.idea.utils.actions.SonarActionUtils;
import org.sonar.ide.shared.SonarUrlUtils;

/**
 * @author Evgeny Mandrikov
 * @see org.sonar.ide.idea.SelectInSonarTarget
 */
public final class GoToDashboardAction extends SonarAction {
  @Override
  public void actionPerformed(AnActionEvent event) {
    String url = SonarUrlUtils.getDashboard(
        SonarActionUtils.getSonarServer(event).getHost(),
        SonarActionUtils.getResourceKey(event)
    );
    BrowserUtil.launchBrowser(url);
  }
}
