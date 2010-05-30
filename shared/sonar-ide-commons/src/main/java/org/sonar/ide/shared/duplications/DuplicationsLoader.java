package org.sonar.ide.shared.duplications;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.api.SonarIdeException;
import org.sonar.ide.shared.SourceCodeMatcher;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public final class DuplicationsLoader {
  private static final Logger LOG = LoggerFactory.getLogger(DuplicationsLoader.class);

  public static final String DUPLICATIONS_DATA = "duplications_data";

  /**
   * Returns violations from specified sonar for specified resource and source code.
   *
   * @param sonar       sonar
   * @param resourceKey resource key
   * @param lines       source code
   * @return duplications
   */
  public static List<Duplication> getDuplications(Sonar sonar, String resourceKey, String[] lines) {
    LOG.info("Loading duplications for {}", resourceKey);
    Resource resource = sonar.find(ResourceQuery.createForMetrics(resourceKey, DUPLICATIONS_DATA));
    Measure measure = resource.getMeasure(DUPLICATIONS_DATA);
    List<Duplication> duplications = parse(measure.getData());
    LOG.info("Loaded {} violations: {}", duplications.size(), duplications);
    SourceQuery sourceQuery = new SourceQuery(resourceKey);
    Source source = sonar.find(sourceQuery);
    return convertLines(duplications, source, lines);
  }

  public static List<Duplication> getDuplications(Sonar sonar, String resourceKey, String text) {
    String[] lines = text.split("\n");
    return getDuplications(sonar, resourceKey, lines);
  }

  public static List<Duplication> convertLines(Collection<Duplication> duplications, Source source, String[] lines) {
    List<Duplication> result = new ArrayList<Duplication>();
    SourceCodeMatcher codeMatcher = new SourceCodeMatcher(source, lines);
    for (Duplication duplication : duplications) {
      int newLine = codeMatcher.match(duplication.getStart());
      if (newLine != -1) {
        duplication.setStart(newLine);
        // TODO convert targetStart 
        result.add(duplication);
      }
    }
    return result;
  }

  private static List<Duplication> parse(String xml) {
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

  public static String getDescription(Duplication duplication) {
    return String.format(
        "Duplicates %s lines from %s starting from %s",
        duplication.getLines(),
        duplication.getTargetResource(),
        duplication.getTargetStart()
    );
  }

  /**
   * Hide utility-class constructor.
   */
  private DuplicationsLoader() {
  }
}
