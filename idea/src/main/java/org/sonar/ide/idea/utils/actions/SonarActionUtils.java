package org.sonar.ide.idea.utils.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.idea.maven.utils.actions.MavenActionUtil;
import org.sonar.ide.idea.utils.IdeaResourceUtils;
import org.sonar.ide.idea.utils.SonarUtils;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;

/**
 * @author Evgeny Mandrikov
 */
public final class SonarActionUtils {
  public static VirtualFile getVirtualFile(AnActionEvent event) {
    return event.getData(PlatformDataKeys.VIRTUAL_FILE);
  }

  public static String getResourceKey(AnActionEvent event) {
//    MavenProject mavenProject = MavenActionUtil.getMavenProject(event);
    Project project = MavenActionUtil.getProject(event);
    VirtualFile virtualFile = getVirtualFile(event);
    PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
    if (psiFile instanceof PsiJavaFile) {
      return IdeaResourceUtils.getInstance().getResourceKey((PsiJavaFile) psiFile);
    }
    return null;
//    return IdeaResourceUtils.getInstance().getProjectKey(mavenProject);
  }

  public static Sonar getSonar(AnActionEvent event) {
    Project project = MavenActionUtil.getProject(event);
    return SonarUtils.getSonar(project);
  }

  public static Host getSonarServer(AnActionEvent event) {
    Project project = MavenActionUtil.getProject(event);
    return SonarUtils.getSonarSettings(project).getServer();
  }

  /**
   * Hide utility-class constructor.
   */
  private SonarActionUtils() {
  }
}
