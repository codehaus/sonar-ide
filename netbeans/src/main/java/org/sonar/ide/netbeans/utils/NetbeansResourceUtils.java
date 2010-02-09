package org.sonar.ide.netbeans.utils;

import org.apache.commons.lang.NotImplementedException;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.modules.maven.model.Utilities;
import org.netbeans.modules.maven.model.pom.POMModel;
import org.netbeans.modules.maven.model.pom.POMModelFactory;
import org.netbeans.modules.xml.xam.ModelSource;
import org.openide.filesystems.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.ide.shared.AbstractResourceUtils;

/**
 * @author Evgeny Mandrikov
 */
public class NetbeansResourceUtils extends AbstractResourceUtils<FileObject> {
  private static final Logger LOG = LoggerFactory.getLogger(NetbeansResourceUtils.class);

  @Override
  protected boolean isJavaFile(FileObject file) {
    return "java".equals(file.getExt());
  }

  @Override
  public String getFileName(FileObject file) {
    return isJavaFile(file) ?
        file.getName() :
        file.getNameExt();
  }

  @Override
  public String getPackageName(FileObject fileObject) {
    ClassPath classPath = ClassPath.getClassPath(fileObject, ClassPath.SOURCE);
    FileObject dir = fileObject.getParent();
    return classPath.getResourceName(dir, '.', false);
  }

  @Override
  protected String getDirectoryPath(FileObject file) {
    // TODO implement me
    throw new NotImplementedException("Currently only java files supported");
  }

  @Override
  public String getProjectKey(FileObject fileObject) {
    Project project = FileOwnerQuery.getOwner(fileObject);
    LOG.info("For file {} Found project {}", fileObject, project);
    FileObject projectDirectory = project.getProjectDirectory();
    LOG.info("Project directory is {}", projectDirectory);
    FileObject pomFileObject = projectDirectory.getFileObject("pom.xml");
    LOG.info("Pom file {}", pomFileObject);
    ModelSource source = Utilities.createModelSource(pomFileObject);
    POMModel model = POMModelFactory.getDefault().getModel(source);
    String groupId = model.getProject().getGroupId();
    String artifactId = model.getProject().getArtifactId();
    return getProjectKey(groupId, artifactId);
  }
}
