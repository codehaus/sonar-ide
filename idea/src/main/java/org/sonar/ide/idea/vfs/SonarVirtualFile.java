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

package org.sonar.ide.idea.vfs;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * @author Evgeny Mandrikov
 */
public class SonarVirtualFile extends VirtualFile {
  private final String resourceKey;

  private final String name;

  private final byte[] content;

  public SonarVirtualFile(String name, byte[] content, String resourceKey) {
    this.name = name;
    this.content = content;
    this.resourceKey = resourceKey;
  }

  @NotNull
  @Override
  public String getName() {
    return name;
  }

  @NotNull
  @Override
  public VirtualFileSystem getFileSystem() {
    return SonarVirtualFileSystem.getInstance();
  }

  @Override
  public String getPath() {
    // TODO
    return null;
  }

  @NotNull
  @Override
  public String getUrl() {
    return SonarVirtualFileSystem.PROTOCOL + "://" + getPath();
  }

  @Override
  public boolean isWritable() {
    return true;
  }

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public VirtualFile getParent() {
    return null;
  }

  @Override
  public VirtualFile[] getChildren() {
    return new VirtualFile[0];
  }

  @NotNull
  @Override
  public OutputStream getOutputStream(Object o, long l, long l1) throws IOException {
    return new ByteArrayOutputStream();
  }

  @NotNull
  @Override
  public byte[] contentsToByteArray() throws IOException {
    return content;
  }

  @Override
  public long getTimeStamp() {
    return 0;
  }

  @Override
  public long getLength() {
    return content.length;
  }

  @Override
  public void refresh(boolean b, boolean b1, Runnable runnable) {
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(content);
  }

  @Override
  public long getModificationStamp() {
    return 0L;
  }

  public String getResourceKey() {
    return resourceKey;
  }
}
