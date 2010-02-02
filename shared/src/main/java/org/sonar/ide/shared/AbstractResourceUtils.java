package org.sonar.ide.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <ul>
 * <li><strong>Java file:</strong> groupId:artifactId:packageName.fileNameWithoutExt
 * <ul>
 * <li>test:test:org.sonar.Foo</li>
 * <li>test:test:[default].Bar</li>
 * </ul>
 * </li>
 * <li><strong>File:</strong> groupId:artifactId:directoryPath/fileName
 * <ul>
 * <li>test:test:org/sonar/foo.sql</li>
 * <li>test:test:[root]/bar.sql</li>
 * </ul>
 * </li>
 *
 * @author Evgeny Mandrikov
 */
public abstract class AbstractResourceUtils<MODEL> {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractResourceUtils.class);

  /**
   * Default package name for classes without package definition.
   */
  public static final String DEFAULT_PACKAGE_NAME = "[default]";

  public static final String ROOT = "[root]";

  public static final char DELIMITER = ':';

  /**
   * Returns resource key of specified file.
   *
   * @param file file
   * @return resource key or null, if unable to determine
   */
  public final String getResourceKey(MODEL file) {
    String resourceKey = null;
    String projectKey = getProjectKey(file);
    if (projectKey != null) {
      String packageName = getPackageName(file);
      if (packageName == null || packageName.trim().equals("")) {
        packageName = DEFAULT_PACKAGE_NAME;
      }
      String fileName = getFileName(file);
      resourceKey = new StringBuilder()
          .append(projectKey).append(DELIMITER).append(packageName).append('.').append(fileName)
          .toString();
    }
    LOG.info("Resource key for {} is {}", file, resourceKey);
    return resourceKey;
  }

  /**
   * Returns name of specified file.
   *
   * @param file file
   * @return filename
   */
  public abstract String getFileName(MODEL file);

  /**
   * Returns package name of specified file.
   *
   * @param file file
   * @return package name or null, if default package
   */
  public abstract String getPackageName(MODEL file);

  /**
   * Returns project key of specified file..
   *
   * @param file file
   * @return project key or null, if unable to determine
   */
  public abstract String getProjectKey(MODEL file);

}
