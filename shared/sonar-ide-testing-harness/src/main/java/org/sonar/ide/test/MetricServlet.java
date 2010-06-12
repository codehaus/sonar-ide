package org.sonar.ide.test;

import org.apache.commons.io.FileUtils;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Evgeny Mandrikov
 */
public class MetricServlet extends GenericServlet {
  @Override
  public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
    PrintWriter out = res.getWriter();
    String json = FileUtils.readFileToString(new File(TestServlet.getBaseDir(this) + "/metrics.json"));
    out.println(json);
  }
}
