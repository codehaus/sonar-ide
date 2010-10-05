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

package org.sonar.ide.netbeans;

import org.openide.modules.ModuleInstall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.netbeans.editor.AnnotationContainer;

/**
 * Manages a module's lifecycle.
 *
 * @author Evgeny Mandrikov
 */
public class SonarModule extends ModuleInstall {
  private static final Logger LOG = LoggerFactory.getLogger(SonarModule.class);

  @Override
  public void restored() {
    LOG.info("Sonar plugin restored");
  }

  @Override
  public void uninstalled() {
    AnnotationContainer.reset();
    LOG.info("Sonar plugin unistalled");
  }
}
