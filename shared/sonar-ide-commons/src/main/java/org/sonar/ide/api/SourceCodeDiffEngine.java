package org.sonar.ide.api;

/**
 * HIGHLY EXPERIMENTAL!!!
 * For now it's just a mapping from Sonar key to content of a local file.
 *
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public interface SourceCodeDiffEngine {

  String getLocalCode(String key);

}
