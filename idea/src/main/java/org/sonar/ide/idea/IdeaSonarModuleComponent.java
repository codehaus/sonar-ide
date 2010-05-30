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

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.ui.AbstractConfigPanel;

import javax.swing.*;
import java.util.Properties;

/**
 * Per-module plugin component.
 *
 * @author Evgeny Mandrikov
 */
public class IdeaSonarModuleComponent extends AbstractConfigurableComponent implements ModuleComponent {

  private String groupId;
  private String artifactId;

  @Nullable
  public static IdeaSonarModuleComponent getInstance(PsiFile file) {
    Module module = ModuleUtil.findModuleForPsiElement(file);
    if (module == null) {
      return null;
    }
    return module.getComponent(IdeaSonarModuleComponent.class);
  }

  public IdeaSonarModuleComponent(final Module module) {
    getLog().info("Loaded component for {}", module);
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public void setArtifactId(String artifactId) {
    this.artifactId = artifactId;
  }

  @Override
  protected AbstractConfigPanel initConfigPanel() {
    // TODO SONARIDE-38
    return new MyConfigPanel("GroupId: " + groupId + " ArtifactId: " + artifactId);
  }

  @Override
  protected void saveConfig(AbstractConfigPanel configPanel) {
  }

  class MyConfigPanel extends AbstractConfigPanel {
    MyConfigPanel(String text) {
      add(new JLabel(text));
    }

    @Override
    public boolean isModified() {
      return false;
    }

    @Override
    public Properties getProperties() {
      return null;
    }
  }
}
