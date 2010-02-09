package org.sonar.ide.idea;

import com.intellij.ide.BrowserUtil;
import com.intellij.ide.SelectInContext;
import com.intellij.ide.SelectInTarget;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.idea.utils.SonarUtils;
import org.sonar.ide.shared.SonarUrlUtils;

/**
 * @author Evgeny Mandrikov
 * @see org.sonar.ide.idea.actions.GoToDashboardAction
 */
public class SelectInSonarTarget implements SelectInTarget {
  @Override
  public boolean canSelect(SelectInContext context) {
    Object selector = context.getSelectorInFile();
    Project project = context.getProject();
    return getSonarUrl(selector, project) != null;
  }

  @Override
  public void selectIn(SelectInContext context, boolean requestFocus) {
    Object selector = context.getSelectorInFile();
    Project project = context.getProject();
    String url = getSonarUrl(selector, project);
    if (url != null) {
      BrowserUtil.launchBrowser(url);
    }
  }

  @Nullable
  private String getSonarUrl(Object selector, Project project) {
    // TODO implement go to project, go to package, go to file
    return SonarUrlUtils.getDashboard(
        SonarUtils.getSonarSettings(project).getServer().getHost(),
        ""
    );
  }

  @Override
  public float getWeight() {
    return Float.MAX_VALUE;
  }

  @Nullable
  @Override
  public String getToolWindowId() {
    return null;
  }

  @Nullable
  @Override
  public String getMinorViewId() {
    return null;
  }

  @Override
  public String toString() {
    return "Sonar";
  }
}
