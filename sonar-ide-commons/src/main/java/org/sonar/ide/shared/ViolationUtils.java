package org.sonar.ide.shared;

import org.sonar.wsclient.services.Violation;

import java.util.*;

/**
 * @author Evgeny Mandrikov
 */
public final class ViolationUtils {
  public static final String PRIORITY_BLOCKER = "blocker";
  public static final String PRIORITY_CRITICAL = "critical";
  public static final String PRIORITY_MAJOR = "major";
  public static final String PRIORITY_MINOR = "minor";
  public static final String PRIORITY_INFO = "info";

  /**
   * Sorts violations by priority in descending order.
   *
   * @param violations list of violations to sort
   * @return sorted list of violations
   */
  public static List<Violation> sortByPriority(List<Violation> violations) {
    Collections.sort(violations, new PriorityComparator());
    return violations;
  }

  public static Map<Integer, List<Violation>> splitByLines(Collection<Violation> violations) {
    Map<Integer, List<Violation>> violationsByLine = new HashMap<Integer, List<Violation>>();
    for (Violation violation : violations) {
      final List<Violation> collection;
      if (violationsByLine.containsKey(violation.getLine())) {
        collection = violationsByLine.get(violation.getLine());
      } else {
        collection = new ArrayList<Violation>();
        violationsByLine.put(violation.getLine(), collection);
      }
      collection.add(violation);
    }
    return violationsByLine;
  }

  /**
   * Converts priority from string to integer.
   *
   * @param priority priority to convert
   * @return converted priority
   */
  public static int convertPriority(String priority) {
    if (PRIORITY_BLOCKER.equalsIgnoreCase(priority)) {
      return 0;
    } else if (PRIORITY_CRITICAL.equalsIgnoreCase(priority)) {
      return 1;
    } else if (PRIORITY_MAJOR.equalsIgnoreCase(priority)) {
      return 2;
    } else if (PRIORITY_MINOR.equalsIgnoreCase(priority)) {
      return 3;
    } else if (PRIORITY_INFO.equalsIgnoreCase(priority)) {
      return 4;
    }
    return 4;
  }

  public static String getDescription(Violation violation) {
    return violation.getRuleName() + " : " + violation.getMessage();
  }

  static class PriorityComparator implements Comparator<Violation> {
    public int compare(Violation o1, Violation o2) {
      int p1 = convertPriority(o1.getPriority());
      int p2 = convertPriority(o2.getPriority());
      return p1 - p2;
    }
  }

  /**
   * Hide utility-class constructor.
   */
  private ViolationUtils() {
  }
}
