package org.sonar.ide.test;

/**
 * @author Evgeny Mandrikov
 */
public class SourceServlet extends TestServlet {
    @Override
    protected String getResource(String classKey) {
        return "/sources/" + classKey + ".json";
    }
}
