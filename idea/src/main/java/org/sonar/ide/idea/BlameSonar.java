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
