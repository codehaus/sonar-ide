package org.sonar.ide.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Evgeny Mandrikov
 */
public abstract class AbstractResourceUtils<MODEL> {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractResourceUtils.class);

  /**
   * Default package name for classes without package definition
   */
  public static final String DEFAULT_PACKAGE_NAME = "[default]";

  public static final char DELIMITER = ':';

  public final String getResourceKey(MODEL file) {
    String projectKey = getProjectKey(file);
    String packageName = getPackageName(file);
    if (packageName == null || packageName.trim().equals("")) {
      packageName = DEFAULT_PACKAGE_NAME;
    }
    String fileName = getFileName(file);
    String resourceKey = new StringBuilder()
        .append(projectKey).append(DELIMITER).append(packageName).append(DELIMITER).append(fileName)
        .toString();
    LOG.info("Resource key for {} is {}", file, resourceKey);
    return resourceKey;
  }

  public abstract String getFileName(MODEL file);

  public abstract String getPackageName(MODEL file);

  public abstract String getProjectKey(MODEL file);

}
