/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Sonar-IDE is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar-IDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar-IDE; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.api;

/**
 * Runtime exception.
 * Good practice is to always specify detail message for exception,
 * so there is no constructors without parameter "message".
 *
 * @author Evgeny Mandrikov
 */
public class SonarIdeException extends RuntimeException {
  /**
   * Constructs a new runtime exception with the specified detail message.
   *
   * @param message the detail message
   */
  public SonarIdeException(String message) {
    super(message);
  }

  /**
   * Constructs a new runtime exception with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause   the cause
   */
  public SonarIdeException(String message, Throwable cause) {
    super(message, cause);
  }
}
