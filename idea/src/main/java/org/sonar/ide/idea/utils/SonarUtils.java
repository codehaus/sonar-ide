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

package org.sonar.ide.idea.utils;

import com.intellij.openapi.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.IdeaSonarProjectComponent;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;

/**
 * @author Evgeny Mandrikov
 */
public final class SonarUtils {
  private static final Logger LOG = LoggerFactory.getLogger(SonarUtils.class);

  /**
   * Hide utility-class constructor.
   */
  private SonarUtils() {
  }

  public static Host getServer(Project project) {
    return IdeaSonarProjectComponent.getInstance(project).getServer();
  }

  public static Sonar getSonar(Project project) {
    Host server = getServer(project);
    LOG.debug("Sonar server: {}", server.getHost());
    return new Sonar(new HttpClient4Connector(server));
  }
}
