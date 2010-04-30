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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.IconButton;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.IconLoader;
import org.sonar.ide.idea.utils.IdeaIconLoader;
import org.sonar.ide.idea.utils.actions.SonarAction;
import org.sonar.ide.idea.utils.actions.SonarActionUtils;
import org.sonar.ide.ui.MeasuresViewer;
import org.sonar.wsclient.Sonar;

import javax.swing.*;

/**
 * @author Evgeny Mandrikov
 */
public class ShowMeasuresAction extends SonarAction {
  @Override
  public void actionPerformed(AnActionEvent event) {
    Sonar sonar = SonarActionUtils.getSonar(event);
    String resourceKey = SonarActionUtils.getResourceKey(event);
    JComponent panel = new MeasuresViewer(sonar, IdeaIconLoader.INSTANCE, resourceKey);
    JBPopup popup = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, panel)
        .setRequestFocus(true)
        .setCancelOnClickOutside(false)
        .setCancelOnOtherWindowOpen(false)
        .setCancelButton(new IconButton("Close", IconLoader.getIcon("/actions/cross.png")))
        .setMovable(true)
        .setTitle(resourceKey)
        .setResizable(true)
        .setCancelKeyEnabled(true)
        .createPopup();
    popup.showInBestPositionFor(event.getDataContext());
  }
}
