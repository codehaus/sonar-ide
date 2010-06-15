package org.sonar.ide.api;

import org.sonar.ide.shared.coverage.CoverageData;
import org.sonar.ide.shared.duplications.Duplication;
import org.sonar.ide.shared.measures.MeasureData;
import org.sonar.wsclient.services.Source;
import org.sonar.wsclient.services.Violation;

import java.util.List;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public interface Measurable {

  List<MeasureData> getMeasures();

  /*
   * TODO Godin:
   * I'm not sure that following methods should be here.
   * Actually those methods work only for files.
   */

  CoverageData getCoverage();

  List<Violation> getViolations();

  List<Duplication> getDuplications();

  Source getCode();

}
