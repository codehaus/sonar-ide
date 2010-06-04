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

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class SourceCodeMatcherTest {

  @Test
  public void testGetHashCode() {
    int hash1 = SourceCodeMatcher.getHashCode("int i;");
    int hash2 = SourceCodeMatcher.getHashCode("int\ti;");
    int hash3 = SourceCodeMatcher.getHashCode("int i;\n");
    int hash4 = SourceCodeMatcher.getHashCode("int i;\r\n");
    int hash5 = SourceCodeMatcher.getHashCode("int i;\r");

    assertThat(hash2, equalTo(hash1));
    assertThat(hash3, equalTo(hash1));
    assertThat(hash4, equalTo(hash1));
    assertThat(hash5, equalTo(hash1));
  }

}
