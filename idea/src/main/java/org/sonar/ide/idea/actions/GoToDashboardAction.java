package org.sonar.ide.idea.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.sonar.ide.idea.utils.actions.SonarAction;
import org.sonar.ide.idea.utils.actions.SonarActionUtils;

/**
 * @author Evgeny Mandrikov
 */
public final class GoToDashboardAction extends SonarAction {
  @Override
  public void actionPerformed(AnActionEvent event) {
    String projectKey = SonarActionUtils.getResourceKey(event);
    String url = new StringBuilder(SonarActionUtils.getSonarServer(event).getHost())
        .append("/project/index/").append(projectKey)
        .toString();
    BrowserUtil.launchBrowser(url);
  }
}
