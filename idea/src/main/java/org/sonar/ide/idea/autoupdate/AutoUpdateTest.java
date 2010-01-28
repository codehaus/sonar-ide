package org.sonar.ide.idea.autoupdate;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.startup.StartupActionScriptManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

import java.io.File;
import java.io.IOException;

/**
 * @author Evgeny Mandrikov
 */
public class AutoUpdateTest {
  /*
  Document doc = new SAXReader().read("http://snapshots.repository.codehaus.org/org/codehaus/sonar-plugins/sonar-ldap-plugin/maven-metadata.xml");
  Node node = doc.getRootElement().element("versioning").element("lastUpdated");
  System.out.println(node.getText());
 */

  public void run() {
    try {
      downloadPlugin(new File(PathManager.getPluginsPath()));

      PluginId pluginId = PluginManager.getPluginByClassName(getClass().getName());
      addActions(pluginId, new File("/tmp/update/test.zip"));
      promptShutdownAndShutdown();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void downloadPlugin(File dir) {

  }

  private void addActions(PluginId pluginId, File localArchiveFile) throws IOException {
    if (PluginManager.isPluginInstalled(pluginId)) {
      IdeaPluginDescriptor pluginDescriptor = PluginManager.getPlugin(pluginId);
      File oldFile = pluginDescriptor.getPath();
      StartupActionScriptManager.ActionCommand deleteOld = new StartupActionScriptManager.DeleteCommand(oldFile);
      StartupActionScriptManager.addActionCommand(deleteOld);
    } else {
      // we should not be here
    }

    String unzipPath = PathManager.getPluginsPath();
    File newFile = new File(unzipPath);
    StartupActionScriptManager.ActionCommand unzip = new StartupActionScriptManager.UnzipCommand(localArchiveFile, newFile);
    StartupActionScriptManager.addActionCommand(unzip);

    StartupActionScriptManager.ActionCommand deleteTemp = new StartupActionScriptManager.DeleteCommand(localArchiveFile);
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

}
