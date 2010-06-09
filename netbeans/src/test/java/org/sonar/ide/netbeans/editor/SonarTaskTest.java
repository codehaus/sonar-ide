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
