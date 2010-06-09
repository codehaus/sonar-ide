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

import org.netbeans.api.java.source.CancellableTask;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.support.EditorAwareJavaSourceTaskFactory;
import org.openide.filesystems.FileObject;

/**
 * @author Evgeny Mandrikov
 */
public class SonarTaskFactory extends EditorAwareJavaSourceTaskFactory {
  public SonarTaskFactory() {
    super(JavaSource.Phase.UP_TO_DATE, JavaSource.Priority.LOW);
  }

  @Override
  protected CancellableTask<CompilationInfo> createTask(FileObject file) {
    return new SonarTask(file);
  }
}
