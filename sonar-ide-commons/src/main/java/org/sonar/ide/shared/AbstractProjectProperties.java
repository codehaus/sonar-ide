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

package org.sonar.ide.shared;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * @author Jérémie Lagarde
 */
public abstract class AbstractProjectProperties<MODEL> {

  private static final String                           P_SONAR_SERVER_URL   = "sonarServerUrl";
  private static final String                           P_PROJECT_GROUPID    = "projectGroupId";
  private static final String                           P_PROJECT_ARTIFACTID = "projectArtifactId";

  private static Map<String, AbstractProjectProperties> projectPropertiesMap = new HashMap<String, AbstractProjectProperties>();

  private MODEL                                         project              = null;

  protected AbstractProjectProperties(MODEL project) {
    this.project = project;
    projectPropertiesMap.put(this.getProjectName(), this);
    load();
  }

  public abstract void save();
  public abstract void load();

  public String getUrl() {
    return getProperty(P_SONAR_SERVER_URL, "");
  }

  public void setUrl(String url) {
    setProperty(P_SONAR_SERVER_URL, url);
  }

  public String getGroupId() {
    return getProperty(P_PROJECT_GROUPID, "");
  }

  public void setGroupId(String groupId) {
    setProperty(P_PROJECT_GROUPID, groupId);
  }

  public String getArtifactId() {
    return getProperty(P_PROJECT_ARTIFACTID, getProjectName());
  }

  public void setArtifactId(String artifactId) {
    setProperty(P_PROJECT_ARTIFACTID, artifactId);
  }

  protected MODEL getProject() {
    return project;
  }

  protected static AbstractProjectProperties find(String name) {
    return projectPropertiesMap.get(name);
  }

  protected abstract String getProjectName();

  protected abstract String getProperty(String value, String defaultValue);

  protected abstract void setProperty(String type, String value);

}
