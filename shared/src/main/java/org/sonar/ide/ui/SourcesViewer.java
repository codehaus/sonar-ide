package org.sonar.ide.ui;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Resource;

/**
 * @author Evgeny Mandrikov
 */
public class SourcesViewer extends AbstractViewer {
  public static final String LINES = "lines";
  public static final String NCLOC = "ncloc";
  public static final String FUNCTIONS = "classes";
  public static final String ACCESSORS = "accessors";

  public static final String STATEMENTS = "statements";
  public static final String COMPLEXITY = "complexity";
  public static final String FUNCTION_COMPLEXITY = "function_complexity";

  public static final String COMMENT_LINES_DENSITY = "comment_lines_density";
  public static final String COMMENT_LINES = "comment_lines";
  public static final String COMMENTED_OUT_CODE_LINES = "commented_out_code_lines";

  public static final String PUBLIC_DOCUMENTED_API_DENSITY = "public_documented_api_density";
  public static final String PUBLIC_UNDOCUMENTED_API = "public_undocumented_api";
  public static final String PUBLIC_API = "public_api";

  public SourcesViewer(Sonar sonar, AbstractIconLoader icons, String resourceKey) {
    super(sonar, icons, resourceKey,
        LINES,
        NCLOC,
        FUNCTIONS,
        ACCESSORS,

        STATEMENTS,
        COMPLEXITY,
        FUNCTION_COMPLEXITY,

        COMMENT_LINES_DENSITY,
        COMMENT_LINES,
        COMMENTED_OUT_CODE_LINES,

        PUBLIC_DOCUMENTED_API_DENSITY,
        PUBLIC_UNDOCUMENTED_API,
        PUBLIC_API
    );
  }

  @Override
  public String getTitle() {
    return "Sources";
  }

  @Override
  protected void display(Resource resource) {
    addCell(
        resource.getMeasure(LINES),
        resource.getMeasure(NCLOC),
        resource.getMeasure(FUNCTIONS),
        resource.getMeasure(ACCESSORS)
    );
    addCell(
        resource.getMeasure(STATEMENTS),
        resource.getMeasure(COMPLEXITY),
        resource.getMeasure(FUNCTION_COMPLEXITY)
    );

    addCell(
        resource.getMeasure(COMMENT_LINES_DENSITY),
        resource.getMeasure(COMMENT_LINES),
        resource.getMeasure(COMMENTED_OUT_CODE_LINES)
    );

    addCell(
        resource.getMeasure(PUBLIC_DOCUMENTED_API_DENSITY),
        resource.getMeasure(PUBLIC_UNDOCUMENTED_API),
        resource.getMeasure(PUBLIC_API)
    );
  }
}
