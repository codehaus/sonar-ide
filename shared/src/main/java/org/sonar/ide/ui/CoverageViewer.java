package org.sonar.ide.ui;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;

/**
 * @author Evgeny Mandrikov
 */
public class CoverageViewer extends AbstractViewer {
  private static final String COVERAGE = "coverage";
  private static final String LINE_COVERAGE = "line_coverage";
  private static final String UNCOVERED_LINES = "uncovered_lines";
  private static final String BRANCH_COVERAGE = "branch_coverage";
  private static final String UNCOVERED_CONDITIONS = "uncovered_conditions";

  public CoverageViewer(Sonar sonar, String resourceKey) {
    super(sonar, resourceKey,
        COVERAGE,
        LINE_COVERAGE,
        UNCOVERED_LINES,
        BRANCH_COVERAGE,
        UNCOVERED_CONDITIONS
    );
  }

  @Override
  public String getTitle() {
    return "Coverage";
  }

  @Override
  protected void display(Resource resource) {
    Measure measure = resource.getMeasure(COVERAGE);
    if (measure == null) {
      addBigCell("-");
    } else {
      addBigCell(measure.getFormattedValue());
    }
    addCell(resource.getMeasure(LINE_COVERAGE));
    addCell(resource.getMeasure(UNCOVERED_LINES));
    addCell(resource.getMeasure(BRANCH_COVERAGE));
    addCell(resource.getMeasure(UNCOVERED_CONDITIONS));
  }
}
