package org.sonar.ide.netbeans.tasklist;

import org.netbeans.spi.tasklist.FileTaskScanner;
import org.netbeans.spi.tasklist.Task;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;
import org.sonar.ide.netbeans.utils.NetbeansResourceUtils;
import org.sonar.ide.shared.SonarProperties;
import org.sonar.ide.shared.ViolationUtils;
import org.sonar.ide.shared.ViolationsLoader;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.Violation;

import java.util.ArrayList;
import java.util.Collection;
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

    String resourceKey = new NetbeansResourceUtils().getFileKey(resource);

    SonarProperties sonarProperties = SonarProperties.getInstance();
    Sonar sonar = new Sonar(new HttpClient4Connector(sonarProperties.getServer()));

    Collection<Violation> violations = ViolationsLoader.getViolations(sonar, resourceKey);

    ArrayList<Task> tasks = new ArrayList<Task>();
    for (Violation violation : violations) {
      tasks.add(Task.create(
          resource,
          "org-sonar-ide-netbeans-Task",
          ViolationUtils.getDescription(violation),
          violation.getLine()
      ));
    }

    return tasks;
  }

  @Override
  public void attach(Callback callback) {
    this.callback = callback;
  }
}
