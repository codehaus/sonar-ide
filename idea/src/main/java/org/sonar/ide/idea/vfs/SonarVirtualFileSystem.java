package org.sonar.ide.idea.vfs;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileSystem;
import org.jetbrains.annotations.NotNull;
import org.sonar.ide.api.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class SonarVirtualFileSystem extends VirtualFileSystem implements ApplicationComponent {
  public static final String PROTOCOL = "sonar";

  /**
   * Listeners for file system events.
   */
  private final List<VirtualFileListener> listeners = new ArrayList<VirtualFileListener>();

  // TODO
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
    // TODO
    return new SonarVirtualFile(s, "Hello".getBytes());
  }

  @Override
  public void refresh(boolean b) {
    // TODO

  }

  @Override
  public VirtualFile refreshAndFindFileByPath(@NotNull String s) {
    // TODO
    return new SonarVirtualFile(s, "Hello".getBytes());
  }

  @Override
  public void addVirtualFileListener(@NotNull VirtualFileListener virtualFileListener) {
    listeners.add(virtualFileListener);
  }

  @Override
  public void removeVirtualFileListener(@NotNull VirtualFileListener virtualFileListener) {
    listeners.remove(virtualFileListener);
  }

  @Override
  protected void deleteFile(Object o, @NotNull VirtualFile file) throws IOException {
//    throw new UnsupportedOperationException();
  }

  @Override
  protected void moveFile(Object o, @NotNull VirtualFile file, @NotNull VirtualFile file1) throws IOException {
//    throw new UnsupportedOperationException();
  }

  @Override
  protected void renameFile(Object o, @NotNull VirtualFile file, @NotNull String s) throws IOException {
//    throw new UnsupportedOperationException();
  }

  @Override
  protected VirtualFile createChildFile(Object o, @NotNull VirtualFile file, @NotNull String s) throws IOException {
//    throw new UnsupportedOperationException();
    return new SonarVirtualFile(s, "Hello".getBytes());
  }

  @Override
  protected VirtualFile createChildDirectory(Object o, @NotNull VirtualFile file, @NotNull String s) throws IOException {
//    throw new UnsupportedOperationException();
    return new SonarVirtualFile(s, "Hello".getBytes());
  }

  @Override
  protected VirtualFile copyFile(Object o, @NotNull VirtualFile file, @NotNull VirtualFile file1, @NotNull String s) throws IOException {
//    throw new UnsupportedOperationException();
    return new SonarVirtualFile(s, "Hello".getBytes());
  }

  @Override
  public boolean isReadOnly() {
    return false;
  }

  @NotNull
  @Override
  public String getComponentName() {
    // TODO
    return getClass().getName();
  }

  @Override
  public void initComponent() {
    Logs.INFO.info("!!! INIT VFS !!!");
    // TODO
  }

  @Override
  public void disposeComponent() {
    // TODO
  }
}
