/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.wsclient;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class SimpleSourceCodeDiffEngineTest {

  @Test
  public void testGetHashCode() {
    int hash1 = SimpleSourceCodeDiffEngine.getHashCode("int i;");
    int hash2 = SimpleSourceCodeDiffEngine.getHashCode("int\ti;");
    int hash3 = SimpleSourceCodeDiffEngine.getHashCode("int i;\n");
    int hash4 = SimpleSourceCodeDiffEngine.getHashCode("int i;\r\n");
    int hash5 = SimpleSourceCodeDiffEngine.getHashCode("int i;\r");

    assertThat(hash2, equalTo(hash1));
    assertThat(hash3, equalTo(hash1));
    assertThat(hash4, equalTo(hash1));
    assertThat(hash5, equalTo(hash1));
  }

  @Test
  public void testSplit() {
    assertThat(SimpleSourceCodeDiffEngine.split("\ntest\n").length, is(3));
  }

}
