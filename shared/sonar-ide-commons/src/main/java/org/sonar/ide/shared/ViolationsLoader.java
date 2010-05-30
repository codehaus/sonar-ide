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

package org.sonar.ide.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Source;
import org.sonar.wsclient.services.SourceQuery;
import org.sonar.wsclient.services.Violation;
import org.sonar.wsclient.services.ViolationQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Note: Violation on zero line means violation on whole file.
 *
 * @author Evgeny Mandrikov
 */
public final class ViolationsLoader {
  private static final Logger LOG = LoggerFactory.getLogger(ViolationsLoader.class);

  /**
   * Sets proper line numbers for violations by matching modified source code with code from server.
   *
   * @param violations violations
   * @param source     source code from server
   * @param lines      source code
   * @return violations with proper line numbers
   */
  public static List<Violation> convertLines(Collection<Violation> violations, Source source, String[] lines) {
    List<Violation> result = new ArrayList<Violation>();

    SourceCodeMatcher codeMatcher = new SourceCodeMatcher(source, lines);

    for (Violation violation : violations) {
      Integer originalLine = violation.getLine();
      if (originalLine == null || originalLine == 0) {
        // skip violation on whole file
        // TODO Godin: we can show them on first line
        continue;
      }

      int newLine = codeMatcher.match(originalLine);
      // skip violation, which doesn't match any line
      if (newLine != -1) {
        violation.setLine(newLine);
        result.add(violation);
      }
    }
    return result;
  }

  /**
   * Returns violations from specified sonar for specified resource and source code.
   *
   * @param sonar       sonar
   * @param resourceKey resource key
   * @param lines       source code
   * @return violations
   */
  public static List<Violation> getViolations(Sonar sonar, String resourceKey, String[] lines) {
    LOG.info("Loading violations for {}", resourceKey);
    ViolationQuery violationQuery = ViolationQuery.createForResource(resourceKey);
    Collection<Violation> violations = sonar.findAll(violationQuery);
    LOG.info("Loaded {} violations: {}", violations.size(), ViolationUtils.toString(violations));
    SourceQuery sourceQuery = new SourceQuery(resourceKey);
    Source source = sonar.find(sourceQuery);
    return convertLines(violations, source, lines);
  }

  /**
   * Returns violations from specified sonar for specified resource and source code.
   *
   * @param sonar       sonar
   * @param resourceKey resource key
   * @param text        source code
   * @return violations
   */
  public static List<Violation> getViolations(Sonar sonar, String resourceKey, String text) {
    String[] lines = text.split("\n");
    return getViolations(sonar, resourceKey, lines);
  }

  /**
   * Hide utility-class constructor.
   */
  private ViolationsLoader() {
  }
}
