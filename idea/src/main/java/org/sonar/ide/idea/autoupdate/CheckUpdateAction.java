package org.sonar.ide.idea.autoupdate;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Evgeny Mandrikov
 */
public class CheckUpdateAction extends AnAction {
  @Override
  public void actionPerformed(AnActionEvent anActionEvent) {
    new AutoUpdateTest().run();
  }
}
