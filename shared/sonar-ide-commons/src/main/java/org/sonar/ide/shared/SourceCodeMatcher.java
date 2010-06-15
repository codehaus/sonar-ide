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

import org.sonar.ide.api.SourceCodeDiff;
import org.sonar.ide.wsclient.SimpleSourceCodeDiffEngine;
import org.sonar.wsclient.services.Source;

/**
 * Compares source code from working copy with code from server.
 * Actually this is an implementation of heuristic algorithm - magic happens here.
 *
 * @author Evgeny Mandrikov
 * @deprecated use {@link org.sonar.ide.wsclient.SimpleSourceCodeDiffEngine} instead of it
 */
@Deprecated
public final class SourceCodeMatcher {
  private SourceCodeDiff diff;

  public SourceCodeMatcher(Source source, String[] lines) {
    String[] remote = SimpleSourceCodeDiffEngine.getLines(source);
    diff = SimpleSourceCodeDiffEngine.getInstance().diff(lines, remote);
  }

  /**
   * @return -1 if not found
   */
  public int match(int originalLine) {
    return diff.newLine(originalLine);
  }
}
