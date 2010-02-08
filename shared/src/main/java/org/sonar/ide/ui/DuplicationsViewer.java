package org.sonar.ide.ui;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

/**
 * @author Evgeny Mandrikov
 */
public class DuplicationsViewer extends AbstractViewer {
  public static final String DUPLICATED_LINES_DENSITY = "duplicated_lines_density";
  public static final String LINES = "lines";
  public static final String DUPLICATED_LINES = "duplicated_lines";
  public static final String DUPLICATED_BLOCKS = "duplicated_blocks";

  public DuplicationsViewer(Sonar sonar, String resourceKey) {
    super(sonar, resourceKey,
        DUPLICATED_LINES_DENSITY,
        LINES,
        DUPLICATED_LINES,
        DUPLICATED_BLOCKS
    );
  }

  @Override
  public String getTitle() {
    return "Duplications";
  }

  @Override
  protected void display(Resource resource) {
    Measure measure = resource.getMeasure(DUPLICATED_LINES_DENSITY);
    if (measure == null) {
      addBigCell("0");
    } else {
      addBigCell(measure.getFormattedValue());
    }
    addCell(getDefaultMeasure(resource, LINES, "Lines"));
    addCell(getDefaultMeasure(resource, DUPLICATED_LINES, "Duplicated lines"));
    addCell(getDefaultMeasure(resource, DUPLICATED_BLOCKS, "Duplicated blocks"));
  }

  private Measure getDefaultMeasure(Resource resource, String metric, String label) {
    Measure measure = resource.getMeasure(metric);
    if (measure == null || measure.getValue() == null) {
      measure = new Measure();
      measure.setMetricName(label);
      measure.setValue(0.0);
      measure.setFormattedValue("0");
    }
    return measure;
  }
}
