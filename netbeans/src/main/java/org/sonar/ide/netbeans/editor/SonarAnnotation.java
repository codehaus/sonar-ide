package org.sonar.ide.netbeans.editor;

import org.openide.text.Annotation;
import org.openide.text.NbDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.wsclient.services.Violation;

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class SonarAnnotation extends Annotation {
  private static final Logger LOG = LoggerFactory.getLogger(SonarAnnotation.class);

  private final StyledDocument document;
  private final int lineNumber;
  private final String shortDescription;

  public SonarAnnotation(StyledDocument document, int lineNumber, String shortDescription) {
    this.document = document;
    this.lineNumber = lineNumber;
    this.shortDescription = shortDescription;
  }

  @Override
  public String getAnnotationType() {
    return "org-sonar-ide-netbeans-sonar-annotation";
  }

  @Override
  public String getShortDescription() {
    return shortDescription;
  }

  public void documentAttach() {
    try {
      Position position = document.createPosition(NbDocument.findLineOffset(document, lineNumber));
      NbDocument.addAnnotation(document, position, -1, this);
    } catch (BadLocationException e) {
      LOG.warn(e.getMessage(), e);
    }
  }

  public void documentDetach() {
    NbDocument.removeAnnotation(document, this);
  }

  public static List<SonarAnnotation> convert(StyledDocument document, Collection<Violation> violations) {
    List<SonarAnnotation> annotations = new ArrayList<SonarAnnotation>();
    for (Violation violation : violations) {
      annotations.add(new SonarAnnotation(document, violation.getLine() - 1, violation.getMessage()));
    }
    return annotations;
  }
}
