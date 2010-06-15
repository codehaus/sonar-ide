package org.sonar.ide.api;

/**
 * @author Evgeny Mandrikov
 */
public interface SourceCode extends Comparable<SourceCode>, Measurable {

  String getKey();

}
