package org.sonar.ide.test;

import org.apache.commons.io.IOUtils;

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
    PrintWriter out = response.getWriter();
    String json = IOUtils.toString(ViolationServlet.class.getResourceAsStream("/violations.json"));
    out.println(json);
  }
}
