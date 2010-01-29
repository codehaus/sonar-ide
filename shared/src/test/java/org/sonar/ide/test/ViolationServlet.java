package org.sonar.ide.test;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.sonar.ide.shared.AbstractResourceUtils;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Evgeny Mandrikov
 */
public class ViolationServlet extends GenericServlet {
  @Override
  public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    String resourceKey = request.getParameter("resource");
    String[] parts = resourceKey.split(":");
    String groupId = parts[0];
    String artifactId = parts[1];
    String classKey = parts[2];
    if (classKey.startsWith(AbstractResourceUtils.DEFAULT_PACKAGE_NAME)) {
      classKey = StringUtils.substringAfter(classKey, ".");
    }

    PrintWriter out = response.getWriter();
    String json;
    try {
      json = IOUtils.toString(ViolationServlet.class.getResourceAsStream("/violations/" + classKey + ".json"));
    } catch (Exception e) {
      json = "[]";
    }
    out.println(json);
  }
}
