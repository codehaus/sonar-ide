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

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;

import java.awt.*;

/**
 * @author Evgeny Mandrikov
 */
public class BlameSonar extends ErrorReportSubmitter {
  @Override
  public String getReportActionText() {
    return "Report to Sonar";
  }

  @Override
  public SubmittedReportInfo submit(IdeaLoggingEvent[] ideaLoggingEvents, Component component) {
    // TODO
    StringBuilder description = new StringBuilder();
    for (IdeaLoggingEvent ideaLoggingEvent : ideaLoggingEvents) {
      description.append(ideaLoggingEvent.getMessage()).append('\n');
      description.append(ideaLoggingEvent.getThrowableText()).append('\n');
    }
    BrowserUtil.launchBrowser("mailto:user@sonar.codehaus.org", description.toString());
    return new SubmittedReportInfo(null, "Error report", SubmittedReportInfo.SubmissionStatus.NEW_ISSUE);
  }
}
