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

package org.sonar.ide.idea.autoupdate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.api.SonarIdeException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public final class VersionInfo {
  public static final String LATEST_VERSION_INFO_URL = "http://snapshots.dist.sonar-ide.codehaus.org/idea/versionInfo.properties";
  public static final String STABLE_VERSION_INFO_URL = "http://dist.sonar-ide.codehaus.org/idea/versionInfo.properties";

  private final String version;
  private final String downloadUrl;

  @Nullable
  public static VersionInfo getLatestPluginVersion() {
    InputStream inputStream = null;
    try {
      URL url = new URL(LATEST_VERSION_INFO_URL);
      inputStream = url.openStream();
      Properties props = new Properties();
      props.load(inputStream);
      return new VersionInfo(
          props.getProperty("version"),
          props.getProperty("downloadUrl")
      );
    } catch (MalformedURLException e) {
      throw new SonarIdeException("WTF?", e);
    } catch (IOException e) {
      return null;
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          // nop
        }
      }
    }
  }

  private VersionInfo(String version, String downloadUrl) {
    this.version = version;
    this.downloadUrl = downloadUrl;
  }

  public String getVersion() {
    return version;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).
        append("version", version).
        append("downloadUrl", downloadUrl).
        toString();
  }
}