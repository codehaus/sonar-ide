package org.sonar.ide.shared;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <ul>
 * <li><strong>Java file:</strong> groupId:artifactId:packageName.fileNameWithoutExt
 * <ul>
 * <li>org.example:myproject:[default].Bar</li>
 * <li>org.example:myproject:org.example.mypackage.Foo</li>
 * </ul>
 * </li>
 * <li><strong>File:</strong> groupId:artifactId:directoryPath/fileName
 * <ul>
 * <li>org.example:myproject:[root]/bar.sql</li>
 * <li>org.example:myproject:mydirectory/mysubdirectory/foo.sql</li>
 * </ul>
 * </li>
 *
 * @author Evgeny Mandrikov
 */
public abstract class AbstractResourceUtils<MODEL> {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractResourceUtils.class);

  private static final char DELIMITER = ':';
  private static final char PACKAGE_DELIMITER = '.';
  private static final char PATH_DELIMITER = '/';

  /**
   * Default package name for classes without package definition.
   */
  public static final String DEFAULT_PACKAGE_NAME = "[default]";

  /**
   * Default directory name for files in root directory.
   */
  public static final String ROOT = "[root]";

  /**
   * Returns project key for specified groupId and artifactId.
   *
   * @param groupId    groupId
   * @param artifactId artifactId
   * @return project key or null, if unable to determine
   */
  public final String getProjectKey(String groupId, String artifactId) {
    if (StringUtils.isBlank(groupId) || StringUtils.isBlank(artifactId)) {
      return null;
    }
    return groupId + DELIMITER + artifactId;
  }

  /**
   * Returns component key for specified file.
   * Examples:
   * <ul>
   * <li>org.example:myproject:[default]</li>
   * <li>org.example:myproject:org.example.mypackage</li>
   * <li>org.example:myproject:[root]<li>
   * <li>org.example:myproject:mydirectory/mysubdirectory</li>
   * </ul>
   *
   * @param file file
   * @return component key or null, if unable to determine
   */
  public final String getComponentKey(MODEL file) {
    String result = null;
    String projectKey = getProjectKey(file);
    String componentName;
    if (isJavaFile(file)) {
      componentName = getPackageName(file);
      if (StringUtils.isWhitespace(componentName)) {
        componentName = DEFAULT_PACKAGE_NAME;
      }
    } else {
      componentName = getDirectoryPath(file);
      if (StringUtils.isWhitespace(componentName)) {
        componentName = ROOT;
      }
    }
    if (projectKey != null && componentName != null) {
      result = new StringBuilder()
          .append(projectKey).append(DELIMITER).append(componentName)
          .toString();
    }
    LOG.info("Component key for {} is {}", file, result);
    return result;
  }

  /**
   * Returns file key for specified file.
   * Examples:
   * <ul>
   * <li>org.example:myproject:[default].ClassOnDefaultPackage</li>
   * <li>org.example:myproject:org.example.mypackage.ClassOne</li>
   * <li>org.example:myproject:[root]/foo.sql<li>
   * <li>org.example:myproject:mydirectory/mysubdirectory/bar.sql</li>
   * </ul>
   *
   * @param file file
   * @return file key or null, if unable to determine
   */
  public final String getFileKey(MODEL file) {
    String result = null;
    String componentKey = getComponentKey(file);
    if (componentKey != null) {
      String fileName = getFileName(file);
      result = isJavaFile(file) ?
          componentKey + PACKAGE_DELIMITER + fileName :
          componentKey + PATH_DELIMITER + fileName;
    }
    LOG.info("Resource key for {} is {}", file, result);
    return result;
  }

  /**
   * Returns true, if specified file is a java file, false otherwise.
   *
   * @param file file
   * @return true, if specified file is a java file, false otherwise
   */
  protected abstract boolean isJavaFile(MODEL file);

  /**
   * Returns name for specified file.
   * Examples:
   * <ul>
   * <li>MyClass</li>
   * <li>foo.sql</li>
   * </ul>
   *
   * @param file file
   * @return filename
   */
  protected abstract String getFileName(MODEL file);

  /**
   * Returns package name for specified file.
   * Example: org.example.mypackage
   *
   * @param file file
   * @return package name (empty for default) or null, if unable to determine
   */
  protected abstract String getPackageName(MODEL file);

  /**
   * Returns directory path for specified file.
   * Example: mydirectory/mysubdirectory
   *
   * @param file file
   * @return directory name (empty for root) or null, if unable to determine
   */
  protected abstract String getDirectoryPath(MODEL file);

  /**
   * Returns project key for specified file.
   * Example: org.example:myproject
   *
   * @param file file
   * @return project key or null, if unable to determine
   */
  public abstract String getProjectKey(MODEL file);

}
