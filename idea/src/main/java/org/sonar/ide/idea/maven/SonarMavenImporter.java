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

package org.sonar.ide.idea.maven;

import com.intellij.openapi.module.Module;
import org.jetbrains.idea.maven.importing.MavenImporter;
import org.jetbrains.idea.maven.importing.MavenModifiableModelsProvider;
import org.jetbrains.idea.maven.importing.MavenRootModelAdapter;
import org.jetbrains.idea.maven.project.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.idea.IdeaSonarModuleComponent;

import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Mandrikov
 */
public final class SonarMavenImporter extends MavenImporter {
  private static final Logger LOG = LoggerFactory.getLogger(SonarMavenImporter.class);

  @Override
  public boolean isApplicable(MavenProject mavenProject) {
    // TODO
    return true;
  }

  @Override
  public boolean isSupportedDependency(MavenArtifact mavenArtifact) {
    // TODO
    return false;
  }

  @Override
  public void preProcess(
      Module module,
      MavenProject mavenProject,
      MavenProjectChanges mavenProjectChanges,
      MavenModifiableModelsProvider mavenModifiableModelsProvider
  ) {
    // TODO
    LOG.info("Pre-process maven");
  }

  @Override
  public void process(
      MavenModifiableModelsProvider mavenModifiableModelsProvider,
      Module module,
      MavenRootModelAdapter mavenRootModelAdapter,
      MavenProjectsTree mavenProjectsTree,
      MavenProject mavenProject,
      MavenProjectChanges mavenProjectChanges,
      Map<MavenProject, String> mavenProjectStringMap,
      List<MavenProjectsProcessorTask> mavenProjectsProcessorTasks
  ) {
    // TODO
    LOG.info("Process maven");
    IdeaSonarModuleComponent sonarModule = module.getComponent(IdeaSonarModuleComponent.class);
    MavenId mavenId = mavenProject.getMavenId();
    sonarModule.setGroupId(mavenId.getGroupId());
    sonarModule.setArtifactId(mavenId.getArtifactId());
  }
}
