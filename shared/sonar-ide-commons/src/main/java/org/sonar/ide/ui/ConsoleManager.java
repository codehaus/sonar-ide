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

package org.sonar.ide.ui;

/**
 * @author Jérémie Lagarde
 */
public class ConsoleManager {

  private static ConsoleManager instance = null;
  private final static ISonarConsole SILENT_CONOLE = new SilentSonarConole();
  private ISonarConsole console;

  protected ConsoleManager() {
    instance = this;
  }

  public static ISonarConsole getConsole() {
    if (instance == null)
      return SILENT_CONOLE;

    return instance.create();
  }

  protected ISonarConsole create() {
    if (console == null)
      console = new DefaultConsole();
    return console;
  }

  static private class SilentSonarConole implements ISonarConsole {

    public void logError(String error) {
    }

    public void logRequest(String request) {
    }

    public void logResponse(String response) {
    }

    public void logError(String error, Throwable ex) {
    }

  }
}
