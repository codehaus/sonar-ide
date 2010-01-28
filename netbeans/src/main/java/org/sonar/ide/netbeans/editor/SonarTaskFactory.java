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
