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

package org.sonar.ide.idea.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.sonar.ide.idea.utils.actions.SonarAction;
import org.sonar.ide.idea.utils.actions.SonarActionUtils;
import org.sonar.ide.shared.SonarUrlUtils;

/**
 * @author Evgeny Mandrikov
 * @see org.sonar.ide.idea.SelectInSonarTarget
 */
public final class GoToDashboardAction extends SonarAction {
  @Override
  public void actionPerformed(AnActionEvent event) {
    String url = SonarUrlUtils.getDashboard(
        SonarActionUtils.getSonarServer(event).getHost(),
        SonarActionUtils.getResourceKey(event)
    );
    BrowserUtil.launchBrowser(url);
  }
}
