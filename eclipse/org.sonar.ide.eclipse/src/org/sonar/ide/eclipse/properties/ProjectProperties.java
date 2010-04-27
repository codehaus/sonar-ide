package org.sonar.ide.eclipse.properties;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;
import org.sonar.ide.api.SonarIdeException;
import org.sonar.ide.eclipse.SonarPlugin;
import org.sonar.ide.eclipse.preferences.PreferenceConstants;
import org.sonar.ide.shared.AbstractProjectProperties;

/**
 * @author Jérémie Lagarde
 */
public class ProjectProperties extends AbstractProjectProperties<IProject>{

  private IEclipsePreferences preferences;

  protected ProjectProperties(IProject project) {
    super(project);
  }

  public static ProjectProperties getInstance(IResource resource) {
    if (resource == null) {
      return null;
    }
    IProject project = resource.getProject();
    if (project == null || !project.isAccessible()) {
      return null;
    }
    ProjectProperties projectProperties = (ProjectProperties)find(project.getName());
    if (projectProperties != null) {
      return projectProperties;
    }
    projectProperties = new ProjectProperties(project);
    return projectProperties;
  }

  public void load() {
    IScopeContext projectScope = new ProjectScope(getProject());
    preferences = projectScope.getNode(SonarPlugin.PLUGIN_ID);
  }

  public void save() {
    try {
      preferences.flush();
    } catch (BackingStoreException e) {
      throw new SonarIdeException("preferences.flush()",e);
    }
  }

  @Override
  public String getUrl() {
    String url = super.getUrl();
    if (StringUtils.isBlank(url))
      url = SonarPlugin.getDefault().getPreferenceStore().getString(PreferenceConstants.P_SONAR_SERVER_URL);
    return url;
  }
  
  @Override
  protected String getProjectName() {
    return getProject().getName();
  }

  @Override
  protected String getProperty(String type, String defaultValue) {
    return preferences.get(type, defaultValue);
  }

  @Override
  protected void setProperty(String type, String value) {
    preferences.put(type,value);
  }

}
