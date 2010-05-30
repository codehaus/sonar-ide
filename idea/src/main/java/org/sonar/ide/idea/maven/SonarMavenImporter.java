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
