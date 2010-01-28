package org.sonar.ide.netbeans.editor;

import org.junit.Test;
import org.netbeans.api.java.source.CancellableTask;
import org.openide.filesystems.FileUtil;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

/**
 * @author Evgeny Mandrikov
 */
public class SonarTaskFactoryTest {
  @Test
  public void testFactory() {
    SonarTaskFactory factory = new SonarTaskFactory();

    File file = FileUtil.normalizeFile(new File(System.getProperty("java.io.tmpdir")));

    CancellableTask task1 = factory.createTask(FileUtil.toFileObject(file));
    assertNotNull(task1);

    CancellableTask task2 = factory.createTask(FileUtil.toFileObject(file));
    assertNotNull(task2);

    assertNotSame(task1, task2);
  }
}
