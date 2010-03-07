package org.sonar.ide.ui.treemap;

/**
 * @author Evgeny Mandrikov
 */
public interface TooltipProvider<MODEL> {
  /**
   * Returns tool tip text for specified value.
   *
   * @param value value
   * @return tool tip text for specified value
   */
  String getToolTipText(MODEL value);
}
