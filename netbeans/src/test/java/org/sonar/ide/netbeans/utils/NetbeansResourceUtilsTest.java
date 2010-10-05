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

package org.sonar.ide.netbeans.utils;

import org.mockito.Mockito;
import org.openide.filesystems.FileObject;
import org.sonar.ide.shared.AbstractResourceUtils;
import org.sonar.ide.shared.AbstractResourceUtilsTest;

import static org.mockito.Mockito.when;

/**
 * @author Evgeny Mandrikov
 */
public class NetbeansResourceUtilsTest extends AbstractResourceUtilsTest<FileObject> {
  @Override
  protected FileObject newFileModel(boolean java, String projectKey, String packageOrDirectory, String fileName) {
    FileObject file = Mockito.mock(FileObject.class);
    if (java) {
      when(file.getExt()).thenReturn("java");
      when(file.getName()).thenReturn(fileName);
    } else {
      when(file.getNameExt()).thenReturn(fileName);
    }
    return file;
  }

  @Override
  protected AbstractResourceUtils<FileObject> newUtils(boolean java, String projectKey, String packageOrDirectory, String fileName) {
    NetbeansResourceUtils utils = Mockito.spy(new NetbeansResourceUtils());
    Mockito.doReturn(packageOrDirectory).when(utils).getPackageName(Mockito.<FileObject>any());
    Mockito.doReturn(packageOrDirectory).when(utils).getDirectoryPath(Mockito.<FileObject>any());
    Mockito.doReturn(projectKey).when(utils).getProjectKey(Mockito.<FileObject>any());
    return utils;
  }
}
