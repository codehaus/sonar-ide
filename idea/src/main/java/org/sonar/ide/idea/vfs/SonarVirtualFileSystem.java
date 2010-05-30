package org.sonar.ide.idea.vfs;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Evgeny Mandrikov
 */
public class SonarVirtualFileSystem extends VirtualFileSystem {
  public static final String PROTOCOL = "sonar";

  private static VirtualFileSystem instance = new SonarVirtualFileSystem();

  public static VirtualFileSystem getInstance() {
    return instance;
  }

  @NotNull
  @Override
  public String getProtocol() {
    return PROTOCOL;
  }

  @Override
  public VirtualFile findFileByPath(@NotNull String s) {
    return null;
  }

  @Override
  public void refresh(boolean b) {
  }

  @Override
  public VirtualFile refreshAndFindFileByPath(@NotNull String s) {
    return findFileByPath(s);
  }

  @Override
  public void addVirtualFileListener(@NotNull VirtualFileListener virtualFileListener) {
  }

  @Override
  public void removeVirtualFileListener(@NotNull VirtualFileListener virtualFileListener) {
  }

  @Override
  protected void deleteFile(Object o, @NotNull VirtualFile file) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void moveFile(Object o, @NotNull VirtualFile file, @NotNull VirtualFile file1) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void renameFile(Object o, @NotNull VirtualFile file, @NotNull String s) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected VirtualFile createChildFile(Object o, @NotNull VirtualFile file, @NotNull String s) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected VirtualFile createChildDirectory(Object o, @NotNull VirtualFile file, @NotNull String s) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected VirtualFile copyFile(Object o, @NotNull VirtualFile file, @NotNull VirtualFile file1, @NotNull String s) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isReadOnly() {
    return true;
  }
}
