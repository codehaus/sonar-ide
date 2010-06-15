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

package org.sonar.ide.shared.duplications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.api.SourceCodeDiff;
import org.sonar.ide.wsclient.SimpleSourceCodeDiffEngine;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 * @deprecated use {@link org.sonar.ide.api.SourceCodeSearchEngine} instead of it
 */
@Deprecated
public final class DuplicationsLoader {
  private static final Logger LOG = LoggerFactory.getLogger(DuplicationsLoader.class);

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
    Resource resource = sonar.find(ResourceQuery.createForMetrics(resourceKey, DuplicationUtils.DUPLICATIONS_DATA));
    Measure measure = resource.getMeasure(DuplicationUtils.DUPLICATIONS_DATA);
    if (measure == null) {
      return Collections.emptyList();
    }
    List<Duplication> duplications = DuplicationUtils.parse(measure.getData());
    LOG.info("Loaded {} duplications: {}", duplications.size(), duplications);
    SourceQuery sourceQuery = new SourceQuery(resourceKey);
    Source source = sonar.find(sourceQuery);
    return convertLines(duplications, source, lines);
  }

  public static List<Duplication> getDuplications(Sonar sonar, String resourceKey, String text) {
    String[] lines = text.split("\n");
    return getDuplications(sonar, resourceKey, lines);
  }

  public static List<Duplication> convertLines(Collection<Duplication> duplications, Source source, String[] lines) {
    String[] remote = SimpleSourceCodeDiffEngine.getLines(source);
    SourceCodeDiff diff = SimpleSourceCodeDiffEngine.getInstance().diff(lines, remote);
    return DuplicationUtils.convertLines(duplications, diff);
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
