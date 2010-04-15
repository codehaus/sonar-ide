package org.sonar.ide.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Predefined SLF4j loggers.
 *
 * @author Evgeny Mandrikov
 */
public final class Logs {
    /**
     * Hide utility-class constructor.
     */
    private Logs() {
    }

    public static final Logger INFO = LoggerFactory.getLogger("org.sonar.INFO");
}
