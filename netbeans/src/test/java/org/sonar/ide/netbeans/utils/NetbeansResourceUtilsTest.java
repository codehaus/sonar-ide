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
