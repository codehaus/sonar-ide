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

import com.intellij.codeInspection.InspectionToolProvider;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.inspection.Violations;

/**
 * Per-application plugin component.
 *
 * @author Evgeny Mandrikov
 */
public class IdeaSonarApplicationComponent implements ApplicationComponent, InspectionToolProvider {
  private static final Logger LOG = LoggerFactory.getLogger(IdeaSonarApplicationComponent.class);

  @NotNull
  @Override
  public String getComponentName() {
    return getClass().getSimpleName();
  }

  @Override
  public void initComponent() {
    LOG.info("Init component");
  }

  @Override
  public void disposeComponent() {
    LOG.info("Dispose component");
  }

  @Override
  public Class[] getInspectionClasses() {
    return new Class[]{
        Violations.class,
//        Duplications.class,
//        Dependencies.class
    };
  }
}
