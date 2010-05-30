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

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.utils.IdeaIconLoader;
import org.sonar.ide.ui.AbstractConfigPanel;

import javax.swing.*;

/**
 * @author Evgeny Mandrikov
 */
public abstract class AbstractConfigurableComponent implements Configurable, BaseComponent {
  private AbstractConfigPanel configPanel;

  protected Logger getLog() {
    return LoggerFactory.getLogger(getClass());
  }

  @Override
  public void initComponent() {
    getLog().debug("Init component");
  }

  @Override
  public void disposeComponent() {
    getLog().debug("Dispose component");
  }

  @NotNull
  @Override
  public String getComponentName() {
    return getClass().getName();
  }

  @Nls
  @Override
  public String getDisplayName() {
    return "Sonar";
  }

  @Override
  public Icon getIcon() {
    return IdeaIconLoader.INSTANCE.getIcon("/org/sonar/ide/images/sonarLogo.png");
  }

  @Override
  public String getHelpTopic() {
    return null;
  }

  @Override
  public JComponent createComponent() {
    getLog().debug("Create UI component");
    if (configPanel == null) {
      configPanel = initConfigPanel();
    }
    reset();
    return configPanel;
  }

  @Override
  public boolean isModified() {
//    getLog().info("Is modified");
    return configPanel != null && configPanel.isModified();
  }

  @Override
  public void apply() throws ConfigurationException {
    getLog().info("Apply configuration");
    if (configPanel == null) {
      return;
    }
    saveConfig(configPanel);
  }

  @Override
  public void reset() {
    getLog().info("Reset configuration panel");
    if (configPanel == null) {
      return;
    }
    configPanel.reset();
  }

  @Override
  public void disposeUIResources() {
    getLog().debug("Dispose UI resources");
    configPanel = null;
  }

  protected abstract AbstractConfigPanel initConfigPanel();

  protected abstract void saveConfig(AbstractConfigPanel configPanel);
}
