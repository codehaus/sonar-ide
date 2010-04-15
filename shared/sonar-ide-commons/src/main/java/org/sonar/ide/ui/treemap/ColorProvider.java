package org.sonar.ide.ui.treemap;

import java.awt.*;

/**
 * @author Evgeny Mandrikov
 */
public interface ColorProvider<MODEL> {
    /**
     * Returns color for specified value.
     *
     * @param value value
     * @return color for specified value
     */
    Color getColor(MODEL value);
}
