package org.sonar.ide.test;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Evgeny Mandrikov
 */
public class VersionServlet extends GenericServlet {
    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String json = "{\"id\":\"20100207124430\", \"version\":\"2.0\"}";
        out.println(json);
    }
}
