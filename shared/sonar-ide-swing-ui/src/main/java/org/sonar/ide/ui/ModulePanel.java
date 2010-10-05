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

package org.sonar.ide.ui;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public class ModulePanel extends AbstractConfigPanel {
  private final JTextField artifactId;
  private final JTextField groupId;
  private final JTextField branch;

  public ModulePanel() {
    artifactId = new JTextField();
    groupId = new JTextField();
    branch = new JTextField();

    DefaultFormBuilder formBuilder = new DefaultFormBuilder(new FormLayout(""));
    formBuilder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    formBuilder.appendColumn("right:pref");
    formBuilder.appendColumn("3dlu");
    formBuilder.appendColumn("fill:p:g");
    formBuilder.append("Group ID:", groupId);
    formBuilder.append("Artifact ID:", artifactId);
    formBuilder.append("Branch:", branch);

    add(formBuilder.getPanel());
  }

  public String getArtifactId() {
    return artifactId.getText();
  }

  public void setArtifactId(String artifactId) {
    this.artifactId.setText(artifactId);
  }

  public String getGroupId() {
    return groupId.getText();
  }

  public void setGroupId(String groupId) {
    this.groupId.setText(groupId);
  }

  public String getBranch() {
    return branch.getText();
  }

  public void setBranch(String branch) {
    this.branch.setText(branch);
  }

  @Override
  public boolean isModified() {
    // TODO
    return true;
  }

  @Override
  public Properties getProperties() {
    // TODO
    return null;
  }

  public static void main(String[] args) {
    SwingAppRunner.run(new ModulePanel());
  }
}
