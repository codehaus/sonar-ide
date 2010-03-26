package org.sonar.ide.test;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Evgeny Mandrikov
 */
public abstract class TestServlet extends GenericServlet {
  private static final String DEFAULT_PACKAGE_NAME = "[default]";

  protected String getClassKey(ServletRequest request) {
    String resourceKey = request.getParameter("resource");
    String[] parts = resourceKey.split(":");
    String groupId = parts[0];
    String artifactId = parts[1];
    String classKey = parts[2];
    if (classKey.startsWith(DEFAULT_PACKAGE_NAME)) {
      classKey = StringUtils.substringAfter(classKey, ".");
    }
    return classKey;
  }

  protected abstract String getResource(String classKey);

  @Override
  public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String json;
    try {
      String classKey = getClassKey(request);
      json = IOUtils.toString(ViolationServlet.class.getResourceAsStream(getResource(classKey)));
    } catch (Exception e) {
      json = "[]";
    }
    out.println(json);
  }
}
