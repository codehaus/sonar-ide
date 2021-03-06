/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.eclipse.internal.jdt;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.sonar.ide.eclipse.core.ISonarFile;
import org.sonar.ide.eclipse.core.ISonarProject;
import org.sonar.ide.eclipse.core.ISonarResource;
import org.sonar.ide.eclipse.core.SonarKeyUtils;
import org.sonar.ide.eclipse.internal.EclipseSonar;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

/**
 * @author Jérémie Lagarde
 */
public class JarElementsAdapterFactory implements IAdapterFactory {

  private static Class<?>[] ADAPTER_LIST = { ISonarResource.class, ISonarFile.class, ISonarProject.class };

  public Object getAdapter(Object adaptableObject, Class adapterType) {
    if (adaptableObject instanceof IClassFile) {
      return new SonarClass((IClassFile) adaptableObject);
    } else if(adaptableObject instanceof IPackageFragmentRoot){
      return new SonarJar((IPackageFragmentRoot) adaptableObject);
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  public Class[] getAdapterList() {
    return ADAPTER_LIST;
  }

  private static class SonarJar  extends AbstractSonarJarElement implements ISonarProject {

    private final IPackageFragmentRoot file;
    private String key;
    
    public SonarJar(IPackageFragmentRoot fragmentRoot) {
      file = fragmentRoot;
    }
    
    public String getKey() {
      if(key ==null) {
        key = getProjectKey(file);
      }
      return key;
    }

    public String getName() {
      return file.getElementName();
    }

    public IProject getProject() {
      return file.getJavaProject().getProject();
    }

    public IResource getResource() {
      return file.getResource();
    }

    public String getGroupId() {
      return StringUtils.substringBefore(getKey(), ":");
    }

    public String getArtifactId() {
      return StringUtils.substringAfter(getKey(), ":");
    }

    public String getBranch() {
      // TODO Auto-generated method stub
      return null;
    }
    
    @Override
    public int hashCode() {
      return file.getElementName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return ((obj instanceof SonarJar) && obj != null && (file.getElementName().equals(((SonarJar) obj).file.getElementName())));
    }
  }
  
  private static class SonarClass extends AbstractSonarJarElement implements ISonarFile {

    private final IClassFile file;
    private String key;
    private String name;
    private String packageName;

    public SonarClass(IClassFile file) {
      Assert.isNotNull(file);
      this.file = file;
    }

    public String getKey() {
      if (key == null) {
        IProject project = file.getJavaProject().getProject();
        String projectKey = getProjectKey(file.getParent());
        String packageName = getPackageName();
        key = SonarKeyUtils.classKey(projectKey, packageName, getName());
      }
      return key;
    }

    public String getName() {
      if (name == null)
        name = StringUtils.substringBeforeLast(file.getElementName(), "."); //$NON-NLS-1$
      return name;
    }

    public IProject getProject() {
      return file.getJavaProject().getProject();
    }

    public IResource getResource() {
      return file.getResource();
    }

    @Override
    public int hashCode() {
      return getKey().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return ((obj instanceof SonarClass) && obj != null && (file.getElementName().equals(((SonarClass) obj).file.getElementName())));
    }

    @Override
    public String toString() {
      return getClass().getSimpleName() + " [key=" + getKey() + "]";
    }

    public String getPackageName() {
      if (packageName == null)
        packageName = getPackageName(file);
      return packageName;
    }

    private String getPackageName(IJavaElement javaElement) {
      String packageName = null;
      if (javaElement instanceof IPackageFragmentRoot) {
        packageName = ""; //$NON-NLS-1$
      } else if (javaElement instanceof IPackageFragment) {
        IPackageFragment packageFragment = (IPackageFragment) javaElement;
        packageName = packageFragment.getElementName();
      } if (javaElement instanceof IClassFile) {
        packageName = getPackageName(javaElement.getParent());
      }
      return packageName;
    }

  }

  private static class AbstractSonarJarElement {

    // TODO : Just for dev, need to find a better way!
    private static List<Resource> cachedResources = null;

    protected String getProjectKey(IJavaElement javaElement) {
      if ( !(javaElement instanceof IPackageFragmentRoot))
        return getProjectKey(javaElement.getParent());
      IPackageFragmentRoot jarElement = (IPackageFragmentRoot) javaElement;
      IProject project = jarElement.getJavaProject().getProject();
      if (project.isAccessible() && jarElement.isArchive()) {
        String sonarProject = StringUtils.substringBeforeLast(jarElement.getElementName(), "-");
        if (cachedResources == null) {
          ResourceQuery query = new ResourceQuery().setScopes(Resource.SCOPE_SET).setQualifiers(Resource.QUALIFIER_PROJECT,
              Resource.QUALIFIER_MODULE);
          EclipseSonar index = EclipseSonar.getInstance(project);
          cachedResources = index.getSonar().findAll(query);
        }
        for (Resource resource : cachedResources) {
          if (resource.getKey().contains(sonarProject))
            return resource.getKey();
        }
      }
      return null;
    }
    
  }
}
