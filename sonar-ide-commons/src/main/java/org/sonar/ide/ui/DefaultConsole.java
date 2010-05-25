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

import org.sonar.ide.api.Logs;

/**
 * Console that shows the output of sonar-ws-client requests in logging system.
 *
 * @author Jérémie Lagarde
 */
public class DefaultConsole implements ISonarConsole {

  public void logRequest(String request) {
    Logs.INFO.info(request);
  }

  public void logResponse(String response) {
    Logs.INFO.info(response);
  }

  public void logError(String error) {
    Logs.INFO.error(error);
  }

  public void logError(String error, Throwable ex) {
    Logs.INFO.error(error, ex);
  }

}
