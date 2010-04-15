package org.sonar.ide.shared;

/**
 * @author Evgeny Mandrikov
 */
public class DefaultResourceUtilsTest extends AbstractResourceUtilsTest<DefaultResourceUtilsTest.FileModel> {
  @Override
  protected FileModel newFileModel(boolean java, String projectKey, String packageOrDirectory, String fileName) {
    return new FileModel(java, projectKey, packageOrDirectory, fileName);
  }

  @Override
  protected AbstractResourceUtils<FileModel> newUtils(boolean java, String projectKey, String packageOrDirectory, String fileName) {
    return new ResourceUtils();
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

  static class ResourceUtils extends AbstractResourceUtils<FileModel> {
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
