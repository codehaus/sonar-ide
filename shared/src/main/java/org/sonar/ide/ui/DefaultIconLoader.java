package org.sonar.ide.ui;

import javax.swing.*;
import java.net.URL;

/**
 * @author Evgeny Mandrikov
 */
public class DefaultIconLoader extends AbstractIconLoader {
  @Override
  public Icon getIcon(String path) {
    if (path == null) {
      return null;
    }
    URL url = getClass().getResource(path);
    return new ImageIcon(url);
  }
}
