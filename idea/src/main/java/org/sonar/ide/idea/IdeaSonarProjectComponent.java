package org.sonar.ide.idea;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import org.sonar.ide.idea.autoupdate.PluginDownloader;
import org.sonar.ide.idea.editor.SonarEditorListener;
import org.sonar.ide.shared.SonarProperties;
import org.sonar.ide.ui.AbstractConfigPanel;
import org.sonar.ide.ui.SonarConfigPanel;

/**
 * Per-project plugin component.
 *
 * @author Evgeny Mandrikov
 */
public class IdeaSonarProjectComponent extends AbstractConfigurableComponent {
  private SonarProperties settings;

  public static IdeaSonarProjectComponent getInstance(Project project) {
    return project.getComponent(IdeaSonarProjectComponent.class);
  }

  public IdeaSonarProjectComponent(Project project) {
    getLog().info("Loaded component for {}", project);
    StartupManager.getInstance(project).registerPostStartupActivity(new Runnable() {
      public void run() {
        getLog().info("Start: project initializing");
        initPlugin();
        getLog().info("End: project initialized");
      }
    });
  }

  @Override
  protected AbstractConfigPanel initConfigPanel() {
    return new SonarConfigPanel();
  }

  private void initPlugin() {
    getLog().info("Init plugin");
    settings = new SonarProperties(SonarProperties.getDefaultPath());
    PluginDownloader.checkUpdate();
    EditorFactory.getInstance().addEditorFactoryListener(new SonarEditorListener());
  }

  public SonarProperties getSettings() {
    return settings;
  }
}
