package org.sonar.ide.idea.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.utils.actions.MavenActionUtil;
import org.sonar.ide.idea.utils.IdeaResourceUtils;
import org.sonar.ide.idea.utils.SonarUtils;
import org.sonar.ide.idea.utils.actions.SonarAction;
import org.sonar.wsclient.Server;

/**
 * @author Evgeny Mandrikov
 */
public final class GoToDashboardAction extends SonarAction {
  @Override
  public void actionPerformed(AnActionEvent event) {
    MavenProject mavenProject = MavenActionUtil.getMavenProject(event);
    Project project = MavenActionUtil.getProject(event);
    Server sonarServer = SonarUtils.getSonarSettings(project).getServer();
    String projectKey = IdeaResourceUtils.getInstance().getProjectKey(mavenProject);
    String url = new StringBuilder(sonarServer.getHost())
        .append("/project/index/").append(projectKey)
        .toString();
    BrowserUtil.launchBrowser(url);
  }
}
