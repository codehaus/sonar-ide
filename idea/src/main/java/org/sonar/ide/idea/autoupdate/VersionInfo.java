package org.sonar.ide.idea.autoupdate;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Evgeny Mandrikov
 */
public class VersionInfo {
  public static final String LATEST_VERSION_INFO_URL = "http://snapshots.dist.sonar-ide.codehaus.org/idea/versionInfo.properties";
  public static final String STABLE_VERSION_INFO_URL = "http://dist.sonar-ide.codehaus.org/idea/versionInfo.properties";

  private final String version;
  private final String downloadUrl;

  public static void checkUpdate() {
    final PluginId pluginId = PluginManager.getPluginByClassName(VersionInfo.class.getName());
    final IdeaPluginDescriptor pluginDescriptor = PluginManager.getPlugin(pluginId);
    if (pluginDescriptor == null) {
      // should never happen
      return;
    }
    final VersionInfo versionInfo = getLatestPluginVersion();
    if (versionInfo != null && !pluginDescriptor.getVersion().equals(versionInfo.getVersion())) {
      ApplicationManager.getApplication().invokeLater(new Runnable() {
        public void run() {
          String title = "New Sonar Plugin";
          String message = "New Sonar Plugin version " + versionInfo.getVersion() + " is available.\n" +
              "Do you want to upgrade from " + pluginDescriptor.getVersion() + "?";
          int answer = Messages.showYesNoDialog(message, title, Messages.getQuestionIcon());
          if (answer == DialogWrapper.OK_EXIT_CODE) {
            new PluginDownloader().run(versionInfo);
          }
        }
      });
    }
  }

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
      throw new RuntimeException(e);
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