package org.sonar.ide.idea.editor;

import com.intellij.openapi.util.IconLoader;
import org.sonar.ide.shared.ViolationUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Evgeny Mandrikov
 */
public class ViolationIcons {
  public static final Icon VIOLATION_ICON = IconLoader.getIcon("/org/sonar/ide/images/violation.png");

  public static final Icon PRIORITY_BLOCKER_ICON = IconLoader.getIcon("/org/sonar/ide/images/blocker.gif");
  public static final Icon PRIORITY_CRITICAL_ICON = IconLoader.getIcon("/org/sonar/ide/images/critical.gif");
  public static final Icon PRIORITY_MAJOR_ICON = IconLoader.getIcon("/org/sonar/ide/images/major.gif");
  public static final Icon PRIORITY_MINOR_ICON = IconLoader.getIcon("/org/sonar/ide/images/minor.gif");
  public static final Icon PRIORITY_INFO_ICON = IconLoader.getIcon("/org/sonar/ide/images/info.gif");

  public static final Icon[] PRIORITY_ICON = new Icon[]{
      PRIORITY_BLOCKER_ICON,
      PRIORITY_CRITICAL_ICON,
      PRIORITY_MAJOR_ICON,
      PRIORITY_MINOR_ICON,
      PRIORITY_INFO_ICON
  };

  public static final Color[] PRIORITY_COLOR = new Color[]{
      Color.RED,
      Color.RED,
      Color.YELLOW,
      Color.YELLOW,
      Color.YELLOW
  };

  public static Color getPriorityColor(String priority) {
    return PRIORITY_COLOR[ViolationUtils.convertPriority(priority)];
  }

  public static Icon getPriorityIcon(String priority) {
    return PRIORITY_ICON[ViolationUtils.convertPriority(priority)];
  }
}
