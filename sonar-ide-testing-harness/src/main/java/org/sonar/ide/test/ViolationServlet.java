package org.sonar.ide.test;

/**
 * @author Evgeny Mandrikov
 */
public class ViolationServlet extends TestServlet {
  @Override
  protected String getResource(String classKey) {
    return "/violations/" + classKey + ".json";
  }
}
