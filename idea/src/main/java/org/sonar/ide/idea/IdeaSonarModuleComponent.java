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

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.ui.AbstractConfigPanel;
import org.sonar.ide.ui.ModulePanel;

/**
 * Per-module plugin component.
 *
 * @author Evgeny Mandrikov
 */
@State(
    name = "Sonar", // TODO name
    storages = {
        @Storage(id = "default", file = "$MODULE_FILE$")
    }
)
public class IdeaSonarModuleComponent extends AbstractConfigurableComponent
    implements ModuleComponent, PersistentStateComponent<IdeaSonarModuleComponent.State> {

  /**
   * The implementation of PersistentStateComponent works by serializing public fields and bean properties into an XML format.
   * The following types of values can be persisted: numbers, booleans, strings, collections, maps, enums.
   * See http://confluence.jetbrains.net/display/IDEADEV/Persisting+State+of+Components
   */
  public static class State {
    public String groupId;
    public String artifactId;
    public String branch;

    /**
     * Note that the state class must have a default constructor.
     * It should return the default state of the component (one used if there is nothing persisted in the XML files yet).
     */
    public State() {
      // Defaults:
      groupId = "";
      artifactId = "";
      branch = "";
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      State state = (State) o;
      return artifactId.equals(state.artifactId) && branch.equals(state.branch) && groupId.equals(state.groupId);
    }

    @Override
    public int hashCode() {
      int result = groupId.hashCode();
      result = 31 * result + artifactId.hashCode();
      result = 31 * result + branch.hashCode();
      return result;
    }
  }

  private State state = new State();

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
   * @param module module
   * @return module component for specified module
   */
  public static IdeaSonarModuleComponent getInstance(Module module) {
    return module.getComponent(IdeaSonarModuleComponent.class);
  }

  @Nullable
  public static IdeaSonarModuleComponent getInstance(PsiFile file) {
    Module module = ModuleUtil.findModuleForPsiElement(file);
    if (module == null) {
      return null;
    }
    return getInstance(module);
  }

  public IdeaSonarModuleComponent(final Module module) {
    getLog().info("Loaded component for {}", module);
  }

  @Override
  public void moduleAdded() {
    getLog().debug("Module added");
  }

  @Override
  public void projectOpened() {
    getLog().debug("Project opened");
  }

  @Override
  public void projectClosed() {
    getLog().debug("Project closed");
  }

  public String getGroupId() {
    return state.groupId;
  }

  public void setGroupId(String groupId) {
    state.groupId = groupId;
  }

  public String getArtifactId() {
    return state.artifactId;
  }

  public void setArtifactId(String artifactId) {
    state.artifactId = artifactId;
  }

  public String getBranch() {
    return state.branch;
  }

  public void setBranch(String branch) {
    state.branch = branch;
  }

  @Override
  protected AbstractConfigPanel initConfigPanel() {
    ModulePanel modulePanel = new ModulePanel();
    modulePanel.setGroupId(getGroupId());
    modulePanel.setArtifactId(getArtifactId());
    modulePanel.setBranch(getBranch());
    return modulePanel;
  }

  @Override
  protected void saveConfig(AbstractConfigPanel configPanel) {
    ModulePanel modulePanel = (ModulePanel) configPanel;
    setGroupId(modulePanel.getGroupId());
    setArtifactId(modulePanel.getArtifactId());
    setBranch(modulePanel.getBranch());
  }
}
