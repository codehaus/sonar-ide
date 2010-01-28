package org.sonar.ide.netbeans.editor;

import org.junit.Test;
import org.openide.filesystems.FileUtil;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Evgeny Mandrikov
 */
public class SonarTaskTest {
  @Test
  public void testCancel() throws Exception {
    File file = FileUtil.normalizeFile(new File(System.getProperty("java.io.tmpdir")));

    SonarTask task = new SonarTask(FileUtil.toFileObject(file));

    assertFalse(task.isCanceled());
    task.cancel();
    assertTrue(task.isCanceled());
    task.cancel();
    assertTrue(task.isCanceled());
    task.init();
    assertFalse(task.isCanceled());

    task.cancel();
    task.run(null);
    assertFalse(task.isCanceled());
  }
}
