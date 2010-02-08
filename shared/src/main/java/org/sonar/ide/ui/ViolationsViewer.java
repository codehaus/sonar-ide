package org.sonar.ide.ui;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Resource;

/**
 * TODO
 *
 * @author Evgeny Mandrikov
 */
public class ViolationsViewer extends AbstractViewer {
  public ViolationsViewer(Sonar sonar, String resourceKey) {
    super(sonar, resourceKey);
  }

  @Override
  public String getTitle() {
    // TODO
    return null;
  }

  @Override
  protected void display(Resource resource) {
    // TODO
  }
}
