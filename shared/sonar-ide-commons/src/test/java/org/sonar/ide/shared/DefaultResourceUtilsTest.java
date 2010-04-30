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
