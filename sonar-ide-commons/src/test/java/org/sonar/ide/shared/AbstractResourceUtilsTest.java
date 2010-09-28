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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Evgeny Mandrikov
 */
public abstract class AbstractResourceUtilsTest<MODEL> {

  @Test
  public void testGetProjectKey() {
    AbstractResourceUtils<MODEL> utils = newUtils(true, null, "org.sonar", "ClassOne");
    assertThat(utils.getProjectKey("org.example", "myproject", null), is("org.example:myproject"));
    assertThat(utils.getProjectKey("org.example", "myproject", ""), is("org.example:myproject"));
    assertThat(utils.getProjectKey("org.example", "myproject", "BRANCH-1.0"), is("org.example:myproject:BRANCH-1.0"));
    assertThat(utils.getProjectKey("", "myproject", "BRANCH-1.0"), nullValue());
    assertThat(utils.getProjectKey("org.example", "", "BRANCH-1.0"), nullValue());
    assertThat(utils.getProjectKey(null, "myproject", "BRANCH-1.0"), nullValue());
    assertThat(utils.getProjectKey("org.example", null, "BRANCH-1.0"), nullValue());
  }

  @Test
  public void testNullProject() {
    AbstractResourceUtils<MODEL> utils = newUtils(true, null, "org.sonar", "ClassOne");
    MODEL file = newFileModel(true, null, "org.sonar", "ClassOne");
    assertThat(utils.getProjectKey(file), nullValue());
    assertThat(utils.getComponentKey(file), nullValue());
    assertThat(utils.getFileKey(file), nullValue());
  }

  @Test
  public void testNullPackage() {
    AbstractResourceUtils<MODEL> utils = newUtils(true, "test:test", null, "ClassOne");
    MODEL file = newFileModel(true, "test:test", null, "ClassOne");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), nullValue());
    assertThat(utils.getFileKey(file), nullValue());
  }

  @Test
  public void testDefaultPackage() throws Exception {
    AbstractResourceUtils<MODEL> utils = newUtils(true, "test:test", "", "ClassOnDefaultPackage");
    MODEL file = newFileModel(true, "test:test", "", "ClassOnDefaultPackage");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), is("test:test:[default]"));
    assertThat(utils.getFileKey(file), is("test:test:[default].ClassOnDefaultPackage"));
  }

  @Test
  public void testSimpleClass() throws Exception {
    AbstractResourceUtils<MODEL> utils = newUtils(true, "test:test", "org.sonar", "ClassOne");
    MODEL file = newFileModel(true, "test:test", "org.sonar", "ClassOne");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), is("test:test:org.sonar"));
    assertThat(utils.getFileKey(file), is("test:test:org.sonar.ClassOne"));
  }

  @Test
  public void testNullDirectory() {
    AbstractResourceUtils<MODEL> utils = newUtils(false, "test:test", null, "File.sql");
    MODEL file = newFileModel(false, "test:test", null, "File.sql");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), nullValue());
    assertThat(utils.getFileKey(file), nullValue());
  }

  @Test
  public void testDefaultDirectory() {
    AbstractResourceUtils<MODEL> utils = newUtils(false, "test:test", "", "FileInRootDirectory.sql");
    MODEL file = newFileModel(false, "test:test", "", "FileInRootDirectory.sql");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), is("test:test:[root]"));
    assertThat(utils.getFileKey(file), is("test:test:[root]/FileInRootDirectory.sql"));
  }

  @Test
  public void testSimpleFile() {
    AbstractResourceUtils<MODEL> utils = newUtils(false, "test:test", "foo/bar", "File.sql");
    MODEL file = newFileModel(false, "test:test", "foo/bar", "File.sql");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), is("test:test:foo/bar"));
    assertThat(utils.getFileKey(file), is("test:test:foo/bar/File.sql"));
  }

  protected abstract MODEL newFileModel(boolean java, String projectKey, String packageOrDirectory, String fileName);

  protected abstract AbstractResourceUtils<MODEL> newUtils(boolean java, String projectKey, String packageOrDirectory, String fileName);
}
