package org.sonar.ide.shared;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class AbstractResourceUtilsTest {
  private ResourceUtils utils;

  @Before
  public void setUp() {
    utils = new ResourceUtils();
  }

  @Test
  public void testNullProject() {
    FileModel file = new FileModel(true, null, "org.sonar", "ClassOne");
    assertThat(utils.getProjectKey(file), nullValue());
    assertThat(utils.getComponentKey(file), nullValue());
    assertThat(utils.getFileKey(file), nullValue());
  }

  @Test
  public void testNullPackage() {
    FileModel file = new FileModel(true, "test:test", null, "ClassOne");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), nullValue());
    assertThat(utils.getFileKey(file), nullValue());
  }

  @Test
  public void testDefaultPackage() throws Exception {
    FileModel file = new FileModel(true, "test:test", "", "ClassOnDefaultPackage");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), is("test:test:[default]"));
    assertThat(utils.getFileKey(file), is("test:test:[default].ClassOnDefaultPackage"));
  }

  @Test
  public void testSimpleClass() throws Exception {
    FileModel file = new FileModel(true, "test:test", "org.sonar", "ClassOne");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), is("test:test:org.sonar"));
    assertThat(utils.getFileKey(file), is("test:test:org.sonar.ClassOne"));
  }

  @Test
  public void testNullDirectory() {
    FileModel file = new FileModel(false, "test:test", null, "File.sql");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), nullValue());
    assertThat(utils.getFileKey(file), nullValue());
  }

  @Test
  public void testDefaultDirectory() {
    FileModel file = new FileModel(false, "test:test", "", "FileInRootDirectory.sql");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), is("test:test:[root]"));
    assertThat(utils.getFileKey(file), is("test:test:[root]/FileInRootDirectory.sql"));
  }

  @Test
  public void testSimpleFile() {
    FileModel file = new FileModel(false, "test:test", "foo/bar", "File.sql");
    assertThat(utils.getProjectKey(file), notNullValue());
    assertThat(utils.getComponentKey(file), is("test:test:foo/bar"));
    assertThat(utils.getFileKey(file), is("test:test:foo/bar/File.sql"));
  }

  class FileModel {
    String projectKey;
    String packageName;
    String fileName;
    boolean java;

    FileModel(boolean java, String projectKey, String packageName, String fileName) {
      this.java = java;
      this.projectKey = projectKey;
      this.packageName = packageName;
      this.fileName = fileName;
    }
  }

  class ResourceUtils extends AbstractResourceUtils<FileModel> {
    @Override
    public String getProjectKey(FileModel file) {
      return file.projectKey;
    }

    @Override
    public String getPackageName(FileModel file) {
      return file.packageName;
    }

    @Override
    public String getFileName(FileModel file) {
      return file.fileName;
    }

    @Override
    protected boolean isJavaFile(FileModel file) {
      return file.java;
    }

    @Override
    protected String getDirectoryPath(FileModel file) {
      return file.packageName;
    }
  }
}
