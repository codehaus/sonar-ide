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

package org.sonar.ide.idea.utils.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.idea.maven.utils.actions.MavenActionUtil;

/**
 * @author Evgeny Mandrikov
 */
public abstract class SonarToggleAction extends ToggleAction implements DumbAware {
  @Override
  public void update(AnActionEvent event) {
    super.update(event);
    event.getPresentation().setEnabled(isAvailable(event));
  }

  protected boolean isAvailable(AnActionEvent event) {
    return MavenActionUtil.getProject(event) != null;
  }

  public final boolean isSelected(AnActionEvent event) {
    return isAvailable(event) && doIsSelected(event);
  }

  protected abstract boolean doIsSelected(AnActionEvent event);
}
