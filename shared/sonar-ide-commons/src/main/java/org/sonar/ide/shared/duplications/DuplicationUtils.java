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

}
