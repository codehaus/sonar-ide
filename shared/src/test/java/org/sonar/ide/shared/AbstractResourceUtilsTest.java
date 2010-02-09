package org.sonar.ide.shared;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public abstract class AbstractResourceUtilsTest<MODEL> {
  @Test
  public void testGetProjectKey() {
    AbstractResourceUtils<MODEL> utils = newUtils(true, null, "org.sonar", "ClassOne");
    assertThat(utils.getProjectKey(null, "myproject"), nullValue());
    assertThat(utils.getProjectKey("org.example", null), nullValue());
    assertThat(utils.getProjectKey("org.example", ""), nullValue());
    assertThat(utils.getProjectKey("", "myproject"), nullValue());
    assertThat(utils.getProjectKey("org.example", "myproject"), is("org.example:myproject"));
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
