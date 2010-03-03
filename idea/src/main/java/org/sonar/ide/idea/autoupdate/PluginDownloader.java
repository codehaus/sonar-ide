package org.sonar.ide.idea.autoupdate;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.startup.StartupActionScriptManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.api.SonarIdeException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Evgeny Mandrikov
 */
public class PluginDownloader {
  private static final Logger LOG = LoggerFactory.getLogger(PluginDownloader.class);

  private static final int TIMEOUT = 15000;

  /*
  Document doc = new SAXReader().read("http://snapshots.repository.codehaus.org/org/codehaus/sonar-plugins/sonar-ldap-plugin/maven-metadata.xml");
  Node node = doc.getRootElement().element("versioning").element("lastUpdated");
  System.out.println(node.getText());
 */

  public void run(VersionInfo versionInfo) {
    try {
      downloadAndInstall(versionInfo);
    } catch (IOException e) {
      throw new SonarIdeException("Unable to download and install new plugin version", e);
    }
  }

  private void downloadAndInstall(VersionInfo versionInfo) throws IOException {
    // Download
    File downloadedArchive = downloadPluginFromUrl(
        versionInfo.getDownloadUrl(),
        new File(PathManager.getPluginsPath())
    );
    // Install
    IdeaPluginDescriptor pluginDescriptor = PluginManager.getPlugin(PluginManager.getPluginByClassName(getClass().getName()));
    addActions(pluginDescriptor, downloadedArchive);
    // Restart IntelliJ IDEA
    promptShutdownAndShutdown();
  }

  private File downloadPluginFromUrl(String pluginUrl, final File destinationDir) {
    String destinationName = "idea-sonar-plugin.zip"; // TODO change

    InputStream inputStream = null;
    OutputStream outputStream = null;
    File downloadedFile;
    try {
      downloadedFile = FileUtil.createTempFile("temp_", "tmp");
    } catch (IOException e) {
      throw new SonarIdeException("Unable to create temp file", e);
    }
    LOG.info("Downloading plugin archive from: {}", pluginUrl);
    URLConnection connection;
    try {
      URL url = new URL(pluginUrl);
      connection = url.openConnection();
      connection.setReadTimeout(TIMEOUT);
      connection.setConnectTimeout(TIMEOUT);
      connection.connect();
    } catch (IOException e) {
      throw new SonarIdeException("Unable to open connection for " + pluginUrl, e);
    }
    try {
      inputStream = connection.getInputStream();
      outputStream = new FileOutputStream(downloadedFile);
      StreamUtil.copyStreamContent(inputStream, outputStream);
    } catch (IOException e) {
      throw new SonarIdeException("Unable to download plugin from " + pluginUrl + " to " + downloadedFile, e);
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          LOG.warn("Exception while closing input stream");
        }
      }
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (IOException e) {
          LOG.warn("Exception while closing output stream");
        }
      }
      if (connection instanceof HttpURLConnection) {
        ((HttpURLConnection) connection).disconnect();
      }
    }
    LOG.info("Downloaded file has {} bytes", downloadedFile.length());
    File newFile = new File(destinationDir, destinationName);
    LOG.info("Renaming downloaded file from {} to {}", downloadedFile, newFile);
    rename(downloadedFile, newFile);
    LOG.info("After renaming file has {} bytes", newFile.length());
    return newFile;
  }

  private void rename(File oldFile, File newFile) {
    if (!oldFile.renameTo(newFile)) {
      try {
        FileUtil.copy(oldFile, newFile);
      } catch (IOException e) {
        throw new SonarIdeException("Renaming file from " + oldFile + " to " + newFile + " failed", e);
      } finally {
        if (!oldFile.delete()) {
          LOG.warn("Deleting of file [{}] failed", oldFile);
        }
      }
    }
  }

  private void addActions(IdeaPluginDescriptor pluginDescriptor, File downloadedArchive) throws IOException {
    PluginId pluginId = pluginDescriptor.getPluginId();
    if (PluginManager.isPluginInstalled(pluginId)) {
      File oldFile = pluginDescriptor.getPath();
      StartupActionScriptManager.ActionCommand deleteOld = new StartupActionScriptManager.DeleteCommand(oldFile);
      StartupActionScriptManager.addActionCommand(deleteOld);
    } else {
      // we should not be here
      throw new SonarIdeException("WTF? Plugin not installed?");
    }

    File pluginsPath = new File(PathManager.getPluginsPath());
    StartupActionScriptManager.ActionCommand unzip = new StartupActionScriptManager.UnzipCommand(downloadedArchive, pluginsPath);
    StartupActionScriptManager.addActionCommand(unzip);

    StartupActionScriptManager.ActionCommand deleteTemp = new StartupActionScriptManager.DeleteCommand(downloadedArchive);
    StartupActionScriptManager.addActionCommand(deleteTemp);
  }

  private void promptShutdownAndShutdown() {
    ApplicationManager.getApplication().invokeLater(new Runnable() {
      public void run() {
        String title = "IDEA shutdown";
        String message = "Sonar Plugin has been installed succssfully.\n" +
            "IntelliJ IDEA needs to be restarted to activate the plugin.\n" +
            "Would you like to shutdown IntelliJ IDEA now?";
        int answer = Messages.showYesNoDialog(message, title, Messages.getQuestionIcon());
        if (answer == DialogWrapper.OK_EXIT_CODE) {
          // TODO maybe restart?
          ApplicationManager.getApplication().exit();
        }
      }
    });
  }

  public static void checkUpdate() {
    final PluginId pluginId = PluginManager.getPluginByClassName(VersionInfo.class.getName());
    final IdeaPluginDescriptor pluginDescriptor = PluginManager.getPlugin(pluginId);
    if (pluginDescriptor == null) {
      // should never happen
      return;
    }
    final VersionInfo versionInfo = VersionInfo.getLatestPluginVersion();
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
}
