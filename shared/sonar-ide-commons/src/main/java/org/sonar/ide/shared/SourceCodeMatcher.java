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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.wsclient.services.Source;

import java.util.HashMap;
import java.util.Map;

/**
 * Compares source code from working copy with code from server.
 * Actually this is an implementation of heuristic algorithm - magic happens here.
 *
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public final class SourceCodeMatcher {
  private static final Logger LOG = LoggerFactory.getLogger(SourceCodeMatcher.class);

  /**
   * Original line -> New line
   */
  private Map<Integer, Integer> map = new HashMap<Integer, Integer>();

  public SourceCodeMatcher(Source source, String[] lines) {
    int[] hashCodes = getHashCodes(lines);
    // Works for O(S*L) time, where S - number of lines on server and L - number of lines in working copy.
    for (int originalLine = 1; originalLine <= source.getLines().size(); originalLine++) {
      int newLine = internalMatch(source, hashCodes, originalLine);
      if (newLine != -1) {
        map.put(originalLine, newLine);
      }
    }
  }

  /**
   * @return -1 if not found
   */
  public int match(int originalLine) {
    if (map.containsKey(originalLine)) {
      return map.get(originalLine);
    }
    return -1;
  }

  /**
   * Currently this method just compares hash codes (see {@link #getHashCode(String)}).
   *
   * @return -1 if not found
   */
  private int internalMatch(Source source, int[] hashCodes, int originalLine) {
    int newLine = -1;
    String originalSourceLine = source.getLine(originalLine);
    int originalHashCode = getHashCode(originalSourceLine);
    // line might not exists in working copy
    if (originalLine - 1 < hashCodes.length) {
      if (hashCodes[originalLine - 1] == originalHashCode) {
        newLine = originalLine;
      }
    }
    for (int i = 0; i < hashCodes.length; i++) {
      if (hashCodes[i] == originalHashCode) {
        if (newLine != -1 && newLine != originalLine) {
          // may be more than one match, but we take into account only first
          LOG.warn("Found more than one match for line '{}'", originalSourceLine);
          break;
        }
        newLine = i + 1;
      }
    }
    return newLine;
  }

  /**
   * Returns hash code for specified string after removing whitespaces.
   *
   * @param str string
   * @return hash code for specified string after removing whitespaces
   */
  public static int getHashCode(String str) {
    if (str == null) {
      return 0;
    }
    return StringUtils.deleteWhitespace(str).hashCode();
  }

  /**
   * Returns hash codes for specified strings after removing whitespaces.
   *
   * @param str strings
   * @return hash codes for specified strings after removing whitespaces
   */
  public static int[] getHashCodes(String[] str) {
    int[] hashCodes = new int[str.length];
    for (int i = 0; i < str.length; i++) {
      hashCodes[i] = getHashCode(str[i]);
    }
    return hashCodes;
  }
}
