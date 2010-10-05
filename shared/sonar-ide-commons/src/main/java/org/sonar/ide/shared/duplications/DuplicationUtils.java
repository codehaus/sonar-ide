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

package org.sonar.ide.shared.duplications;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.sonar.ide.api.SonarIdeException;
import org.sonar.ide.api.SourceCodeDiff;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public final class DuplicationUtils {
  public static final String DUPLICATIONS_DATA = "duplications_data";

  public static List<Duplication> parse(String xml) {
    List<Duplication> result = new ArrayList<Duplication>();
    SAXBuilder builder = new SAXBuilder();
    Document doc;
    try {
      doc = builder.build(new StringReader(xml));
    } catch (JDOMException e) {
      throw new SonarIdeException("Unable to parse duplications", e);
    } catch (IOException e) {
      throw new SonarIdeException("Unable to parse duplications", e);
    }
    List duplications = doc.getRootElement().getChildren("duplication");
    for (Object elementObj : duplications) {
      Element element = (Element) elementObj;
      String lines = element.getAttributeValue("lines");
      String start = element.getAttributeValue("start");
      String targetStart = element.getAttributeValue("target-start");
      String targetResource = element.getAttributeValue("target-resource");
      result.add(new Duplication(
          Integer.parseInt(lines),
          Integer.parseInt(start),
          Integer.parseInt(targetStart),
          targetResource
      ));
    }
    return result;
  }

  public static List<Duplication> convertLines(Collection<Duplication> duplications, SourceCodeDiff diff) {
    List<Duplication> result = new ArrayList<Duplication>();
    for (Duplication duplication : duplications) {
      int newLine = diff.newLine(duplication.getStart());
      if (newLine != -1) {
        duplication.setStart(newLine);
        // TODO convert targetStart
        result.add(duplication);
      }
    }
    return result;
  }

  /**
   * Hide utility-class constructor.
   */
  private DuplicationUtils() {
  }

  public static String getDescription(Duplication duplication) {
    return String.format(
        "Duplicates %s lines from %s starting from %s",
        duplication.getLines(),
        duplication.getTargetResource(),
        duplication.getTargetStart()
    );
  }
}
