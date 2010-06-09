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

package org.sonar.ide.netbeans.editor;

import org.openide.text.Annotation;
import org.openide.text.NbDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.shared.ViolationUtils;
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
    super();
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
      annotations.add(new SonarAnnotation(
          document,
          violation.getLine() - 1,
          ViolationUtils.getDescription(violation)
      ));
    }
    return annotations;
  }
}
