package org.sonar.ide.test;

/**
 * @author Evgeny Mandrikov
 */
public class MeasureServlet extends TestServlet {
  @Override
  protected String getResource(String classKey) {
    return "/measures/" + classKey + ".json";
  }
}
