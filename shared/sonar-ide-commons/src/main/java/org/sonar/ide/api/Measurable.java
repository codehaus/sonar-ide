package org.sonar.ide.api;

import java.util.List;

import org.sonar.ide.shared.coverage.CoverageData;
import org.sonar.ide.shared.duplications.Duplication;
import org.sonar.wsclient.services.Source;
import org.sonar.wsclient.services.Violation;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public interface Measurable {

  List<IMeasure> getMeasures();

  /*
   * TODO Godin:
   * I'm not sure that following methods should be here.
   * Actually those methods work only for files.
   */

  List<Violation> getViolations();

  List<Duplication> getDuplications();

  CoverageData getCoverage();

  Source getCode();

}
