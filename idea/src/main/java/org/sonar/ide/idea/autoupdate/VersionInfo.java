package org.sonar.ide.idea.autoupdate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.Nullable;
import org.sonar.ide.shared.SonarIdeException;

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