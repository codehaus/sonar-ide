/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Sonar-IDE is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar-IDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar-IDE; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.idea;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.sonar.ide.idea.editor.SonarEditorListener;
import org.sonar.ide.shared.SonarUrlUtils;
import org.sonar.ide.ui.AbstractConfigPanel;
import org.sonar.ide.ui.MeasuresPanel;
import org.sonar.ide.ui.SonarConfigPanel;
import org.sonar.wsclient.Host;

/**
 * Per-project plugin component.
 *
 * @author Evgeny Mandrikov
 */
@State(
    name = "Sonar", // TODO name
    storages = {
        @Storage(id = "default", file = "$PROJECT_FILE$")
    }
)
public class IdeaSonarProjectComponent extends AbstractConfigurableComponent
    implements ProjectComponent, PersistentStateComponent<IdeaSonarProjectComponent.State> {

  private Project project;
  private State state = new State();

  public static class State {
    public String host;
    public String username;
    public String password;

    public State() {
      // Defaults:
      host = SonarUrlUtils.HOST_DEFAULT;
      username = "";
      password = "";
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      State state = (State) o;
      return host.equals(state.host) && password.equals(state.password) && username.equals(state.username);
    }

    @Override
    public int hashCode() {
      int result = host.hashCode();
      result = 31 * result + username.hashCode();
      result = 31 * result + password.hashCode();
      return result;
    }
  }

  @Override
  public State getState() {
    getLog().debug("Get state");
    return state;
  }

  @Override
  public void loadState(State state) {
    getLog().debug("Load state");
    this.state = state;
  }

  /**
   * @param project project
   * @return project component for specified project
   */
  public static IdeaSonarProjectComponent getInstance(Project project) {
    return project.getComponent(IdeaSonarProjectComponent.class);
  }

  public IdeaSonarProjectComponent(Project project) {
    getLog().info("Loaded component for {}", project);
    this.project = project;
    StartupManager.getInstance(project).registerPostStartupActivity(new Runnable() {
      public void run() {
        getLog().info("Start: project initializing");
        initPlugin();
        getLog().info("End: project initialized");
      }
    });
  }

  @Override
  public void projectOpened() {
    getLog().debug("Project opened");
    registerToolWindow();
  }

  @Override
  public void projectClosed() {
    getLog().debug("Project closed");
    unregisterToolWindow();
  }

  @Override
  protected AbstractConfigPanel initConfigPanel() {
    SonarConfigPanel configPanel = new SonarConfigPanel();
    configPanel.setHost(state.host);
    configPanel.setUsername(state.username);
    configPanel.setPassword(state.password);
    return configPanel;
  }

  @Override
  protected void saveConfig(AbstractConfigPanel configPanel) {
    SonarConfigPanel config = (SonarConfigPanel) configPanel;
    state.host = config.getHost();
    state.username = config.getUsername();
    state.password = config.getPassword();
  }

  private void initPlugin() {
    getLog().info("Init plugin");
    // See SONARIDE-12
//    PluginDownloader.checkUpdate();
    EditorFactory.getInstance().addEditorFactoryListener(new SonarEditorListener());
  }

  public Host getServer() {
    return new Host(state.host).setPassword(state.password).setUsername(state.username);
  }

  private static final String ID_TOOLWINDOW = "Sonar";
  private ToolWindow toolWindow;

  private void registerToolWindow() {
    getLog().debug("Registering tool window");
    final ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
    toolWindow = toolWindowManager.registerToolWindow(ID_TOOLWINDOW, false, ToolWindowAnchor.RIGHT);
    toolWindow.setIcon(getIcon());

    ContentFactory contentFactory = toolWindow.getContentManager().getFactory();
    Content content = contentFactory.createContent(new MeasuresPanel(), "Measures", false);
    toolWindow.getContentManager().addContent(content);
  }

  private void unregisterToolWindow() {
    getLog().debug("Deregistering tool window");
    final ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
    toolWindowManager.unregisterToolWindow(ID_TOOLWINDOW);
  }
}
