/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.idea;

import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.sonar.ide.idea.inspection.Duplications;
import org.sonar.ide.idea.inspection.Violations;
import org.sonar.ide.ui.AbstractConfigPanel;

import java.util.Properties;

/**
 * Per-application plugin component.
 *
 * @author Evgeny Mandrikov
 */
@State(
    name = "Sonar", // TODO name
    storages = {
        @Storage(id = "default", file = "$APP_CONFIG$/sonar.xml")
    }
)
public class IdeaSonarApplicationComponent extends AbstractConfigurableComponent
    implements ApplicationComponent, InspectionToolProvider, PersistentStateComponent<IdeaSonarApplicationComponent.State> {

  private State state = new State();

  public static class State {
    public State() {
    }
  }

  @Override
  public State getState() {
    return state;
  }

  @Override
  public void loadState(State state) {
    this.state = state;
  }

  /**
   * @return application component
   */
  public static IdeaSonarApplicationComponent getInstance() {
    return ApplicationManager.getApplication().getComponent(IdeaSonarApplicationComponent.class);
  }

  @Override
  protected AbstractConfigPanel initConfigPanel() {
    // TODO
    return new MyConfigPanel();
  }

  @Override
  protected void saveConfig(AbstractConfigPanel configPanel) {
    // TODO
  }

  @Override
  public Class[] getInspectionClasses() {
    return new Class[]{
        Violations.class,
        Duplications.class
    };
  }

  class MyConfigPanel extends AbstractConfigPanel {
    @Override
    public boolean isModified() {
      // TODO
      return false;
    }

    @Override
    public Properties getProperties() {
      // TODO
      return null;
    }
  }
}
