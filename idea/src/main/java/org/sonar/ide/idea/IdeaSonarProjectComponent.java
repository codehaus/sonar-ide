package org.sonar.ide.idea;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.autoupdate.VersionInfo;
import org.sonar.ide.idea.editor.SonarEditorListener;

/**
 * Per-project plugin component.
 *
 * @author Evgeny Mandrikov
 */
public class IdeaSonarProjectComponent implements ProjectComponent {
  private static final Logger LOG = LoggerFactory.getLogger(IdeaSonarProjectComponent.class);

  public IdeaSonarProjectComponent(Project project) {
    StartupManager.getInstance(project).registerPostStartupActivity(new Runnable() {
      public void run() {
        LOG.info("Start: project initializing");
        initPlugin();
        LOG.info("End: project initialized");
      }
    });
  }

  @NotNull
  public String getComponentName() {
    return getClass().getSimpleName();
  }

  private void initPlugin() {
    LOG.info("Init plugin");
    VersionInfo.checkUpdate();
    EditorFactory.getInstance().addEditorFactoryListener(new SonarEditorListener());
  }

  public void projectOpened() {
    LOG.info("Project opened");
    // TODO
  }

  public void projectClosed() {
    LOG.info("Project closed");
    // TODO
  }

  public void initComponent() {
    LOG.info("Init component");
    // TODO
  }

  public void disposeComponent() {
    LOG.info("Dispose component");
    // TODO
  }

}
