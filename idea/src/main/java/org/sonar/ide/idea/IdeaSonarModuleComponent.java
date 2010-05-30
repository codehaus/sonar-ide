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
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
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
  private String branch;

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

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  @Override
  protected AbstractConfigPanel initConfigPanel() {
    return new MyConfigPanel(this);
  }

  @Override
  protected void saveConfig(AbstractConfigPanel configPanel) {
    MyConfigPanel myConfigPanel = (MyConfigPanel) configPanel;
    setArtifactId(myConfigPanel.getArtifactId());
    setGroupId(myConfigPanel.getGroupId());
    setBranch(myConfigPanel.getBranch());
  }

  class MyConfigPanel extends AbstractConfigPanel {
    private final JTextField artifactId;
    private final JTextField groupId;
    private final JTextField branch;

    MyConfigPanel(IdeaSonarModuleComponent sonarModule) {
      artifactId = new JTextField(sonarModule.getArtifactId());
      groupId = new JTextField(sonarModule.getGroupId());
      branch = new JTextField(sonarModule.getBranch());

      DefaultFormBuilder formBuilder = new DefaultFormBuilder(new FormLayout(""));
      formBuilder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      formBuilder.appendColumn("right:pref");
      formBuilder.appendColumn("3dlu");
      formBuilder.appendColumn("fill:p:g");
      formBuilder.append("GroupId:", groupId);
      formBuilder.append("ArtifactId:", artifactId);
      formBuilder.append("Branch:", branch);

      add(formBuilder.getPanel());
    }

    @Override
    public boolean isModified() {
      // TODO
      return true;
    }

    @Override
    public Properties getProperties() {
      return null;
    }

    public String getArtifactId() {
      return artifactId.getText();
    }

    public String getGroupId() {
      return groupId.getText();
    }

    public String getBranch() {
      return branch.getText();
    }
  }
}
