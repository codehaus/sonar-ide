package org.sonar.ide.ui;

import org.sonar.ide.shared.ViolationUtils;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Violation;

import java.awt.*;

/**
 * @author Evgeny Mandrikov
 */
public final class IconsUtils {
  private static final Color[] VIOLATION_COLOR = new Color[]{
      Color.RED, // Blocker
      Color.RED, // Critical
      Color.YELLOW, // Major
      Color.YELLOW, // Minor
      Color.YELLOW // Info
  };

  protected static final String IMAGES_PATH = "/org/sonar/ide/images/";

  /**
   * Returns icon for specified violation.
   *
   * @param violation violation
   * @return icon
   */
  public static String getIconPath(Violation violation) {
    return IMAGES_PATH + "violation.png";
  }

  /**
   * Returns priority icon for specified violation.
   *
   * @param violation violation
   * @return priority icon
   */
  public static String getPriorityIconPath(Violation violation) {
    String priority = violation.getPriority();
    return IMAGES_PATH + "priority/" + priority.toLowerCase() + ".gif";
  }

  public static String getTendencyIconPath(Measure measure, boolean small) {
    Integer trend = measure.getTrend();
    Integer var = measure.getVar();
    if (trend == null || var == null) {
      return null;
    }
    if (var == 0) {
      return null;
    }
    // trend = color
    // var = value
    StringBuilder sb = new StringBuilder(IMAGES_PATH);
    sb.append("tendency/");
    sb.append(var);
    switch (trend) {
      case 0:
        sb.append("-black");
        break;
      case -1:
        sb.append("-red");
        break;
      case 1:
        sb.append("-green");
        break;
    }
    if (small) {
      sb.append("-small");
    }
    sb.append(".png");
    return sb.toString();
  }

  /**
   * Returns tendency icon for specified measure.
   *
   * @param measure measure
   * @return tendency icon
   */
  public static String getTendencyIconPath(Measure measure) {
    return getTendencyIconPath(measure, false);
  }

  /**
   * Returns color for specified violation.
   *
   * @param violation violation
   * @return color
   */
  public static Color getColor(Violation violation) {
    String priority = violation.getPriority();
    return VIOLATION_COLOR[ViolationUtils.convertPriority(priority)];
  }

  /**
   * Hide utility-class constructor.
   */
  private IconsUtils() {
  }
}
