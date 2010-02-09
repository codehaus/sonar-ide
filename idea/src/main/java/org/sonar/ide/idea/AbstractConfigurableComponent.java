package org.sonar.ide.idea;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.utils.IdeaIconLoader;
import org.sonar.ide.ui.AbstractConfigPanel;

import javax.swing.*;

/**
 * @author Evgeny Mandrikov
 */
public abstract class AbstractConfigurableComponent implements Configurable, ProjectComponent, ModuleComponent {
  private AbstractConfigPanel configPanel;

  protected Logger getLog() {
    return LoggerFactory.getLogger(getClass());
  }

  @Override
  public void projectOpened() {
    getLog().debug("Project opened");
  }

  @Override
  public void projectClosed() {
    getLog().debug("Project closed");
  }

  @Override
  public void moduleAdded() {
    getLog().debug("Module added");
  }

  @Override
  public void initComponent() {
    getLog().debug("Init component");
  }

  @Override
  public void disposeComponent() {
    getLog().debug("Dispose component");
  }

  @NotNull
  @Override
  public String getComponentName() {
    return getClass().getName();
  }

  @Nls
  @Override
  public String getDisplayName() {
    return "Sonar";
  }

  @Override
  public Icon getIcon() {
    return IdeaIconLoader.INSTANCE.getIcon("/org/sonar/ide/images/sonarLogo.png");
  }

  @Override
  public String getHelpTopic() {
    return null;
  }

  @Override
  public JComponent createComponent() {
    getLog().debug("Create component");
    if (configPanel == null) {
      configPanel = initConfigPanel();
    }
    reset();
    return configPanel;
  }

  @Override
  public boolean isModified() {
    getLog().info("Is modified");
    return configPanel != null && configPanel.isModified();
  }

  @Override
  public void apply() throws ConfigurationException {
    getLog().info("Apply");
    if (configPanel == null) {
      return;
    }
    saveConfig(configPanel);
  }

  @Override
  public void reset() {
    getLog().info("Reset");
    if (configPanel == null) {
      return;
    }
    configPanel.reset();
  }

  @Override
  public void disposeUIResources() {
    getLog().debug("Dispose UI resources");
    configPanel = null;
  }

  protected abstract AbstractConfigPanel initConfigPanel();

  protected abstract void saveConfig(AbstractConfigPanel configPanel);
}
