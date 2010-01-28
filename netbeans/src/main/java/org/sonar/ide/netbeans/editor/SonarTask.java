package org.sonar.ide.netbeans.editor;

import org.netbeans.api.java.source.CancellableTask;
import org.netbeans.api.java.source.CompilationInfo;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.netbeans.utils.ResourceUtils;
import org.sonar.ide.shared.SonarProperties;
import org.sonar.ide.shared.ViolationsLoader;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.Violation;

import javax.swing.text.StyledDocument;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class SonarTask implements CancellableTask<CompilationInfo> {
  private static final Logger LOG = LoggerFactory.getLogger(SonarTask.class);

  private final FileObject fileObject;

  private boolean cancelled;

  public SonarTask(FileObject fileObject) {
    this.fileObject = fileObject;
  }

  public synchronized void cancel() {
    cancelled = true;
  }

  synchronized void init() {
    cancelled = false;
  }

  synchronized boolean isCanceled() {
    return cancelled;
  }

  public void run(CompilationInfo info) throws Exception {
    init();

    DataObject data = DataObject.find(fileObject);
    if (data == null || data.isModified()) {
      return;
    }
    EditorCookie editor = data.getCookie(EditorCookie.class);
    if (editor == null) {
      return;
    }
    StyledDocument document = editor.openDocument();
    // Do job

    String resourceKey = new ResourceUtils().getResourceKey(fileObject);

    SonarProperties sonarProperties = SonarProperties.getInstance();
    Sonar sonar = new Sonar(new HttpClient4Connector(sonarProperties.getServer()));

    ViolationsLoader violationsLoader = new ViolationsLoader();
    Collection<Violation> violations = violationsLoader.getViolations(sonar, resourceKey);
    List<SonarAnnotation> annotations = SonarAnnotation.convert(document, violations);

    // Save results
    if (!isCanceled()) {
      AnnotationContainer container = AnnotationContainer.getInstance(fileObject);
      if (container != null) {
        container.setAnnotations(annotations);
      } else {
        LOG.info("No annotation container");
      }
    }
  }
}
