package org.sonar.ide.ui;

import javax.swing.*;
import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public abstract class AbstractConfigPanel extends JPanel {
  public AbstractConfigPanel() {
    super();
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }

  /**
   * Returns true, if settings have been modified.
   *
   * @return true, if settings have been modified
   */
  public abstract boolean isModified();

  public abstract Properties getProperties();

  /**
   * Resets state of this component.
   */
  public void reset() {
  }
}
