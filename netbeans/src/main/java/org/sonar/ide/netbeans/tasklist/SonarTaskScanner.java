package org.sonar.ide.netbeans.tasklist;

import org.netbeans.spi.tasklist.FileTaskScanner;
import org.netbeans.spi.tasklist.Task;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class SonarTaskScanner extends FileTaskScanner {
  private Callback callback;

  public SonarTaskScanner() {
    super(
        NbBundle.getMessage(SonarTaskScanner.class, "SonarTaskScanner.label"),
        NbBundle.getMessage(SonarTaskScanner.class, "SonarTaskScanner.hint"),
        "Advanced"
    );
  }

  @Override
  public List<? extends Task> scan(FileObject resource) {
    if (resource == null || !"java".equalsIgnoreCase(resource.getExt())) {
      return null;
    }
    ArrayList<Task> tasks = new ArrayList<Task>();
    tasks.add(Task.create(resource, "org-sonar-ide-netbeans-Task", "TEST message", 1));
    return tasks;
  }

  @Override
  public void attach(Callback callback) {
    this.callback = callback;
  }
}
