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
