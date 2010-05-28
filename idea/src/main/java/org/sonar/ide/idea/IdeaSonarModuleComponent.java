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
import org.sonar.ide.idea.maven.SonarMavenUtils;
import org.sonar.ide.ui.AbstractConfigPanel;

import javax.swing.*;
import java.util.Properties;

/**
 * Per-module plugin component.
 *
 * @author Evgeny Mandrikov
 */
public class IdeaSonarModuleComponent extends AbstractConfigurableComponent implements ModuleComponent {
  private Module module;

  public IdeaSonarModuleComponent(final Module module) {
    getLog().info("Loaded component for {}", module);
    this.module = module;
  }

  @Override
  protected AbstractConfigPanel initConfigPanel() {
    // TODO SONARIDE-38
    return new MyConfigPanel(SonarMavenUtils.isMavenModule(module) ?
        "This is a Maven module, so Sonar will be enabled for this module." :
        "Currently non-Maven modules not supported, so Sonar will be disabled for this module."
    );
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
