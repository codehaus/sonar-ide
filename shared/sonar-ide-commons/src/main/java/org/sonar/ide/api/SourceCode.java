package org.sonar.ide.api;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public interface SourceCode extends Comparable<SourceCode>, Measurable {

  String getKey();

  /**
   * HIGHLY EXPERIMENTAL!!!
   *
   * @param content content of this resource
   * @return this (for method chaining)
   */
  SourceCode setLocalContent(String content);

}
