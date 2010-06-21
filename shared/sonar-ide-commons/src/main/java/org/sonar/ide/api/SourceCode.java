package org.sonar.ide.api;

import java.util.Set;

/**
 * @author Evgeny Mandrikov
 * @since 0.2
 */
public interface SourceCode extends Comparable<SourceCode>, Measurable {

  String getKey();

  String getName();

  Set<SourceCode> getChildren();

  /**
   * HIGHLY EXPERIMENTAL!!!
   * 
   * @param content
   *          content of this resource
   * @return this (for method chaining)
   */
  SourceCode setLocalContent(String content);

}
