package org.sonar.ide.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

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
public abstract class TestServlet extends GenericServlet {
    private static final String DEFAULT_PACKAGE_NAME = "[default]";

    protected abstract String getResource(String classKey);

    protected File getResourceAsFile(String testName, String classKey) {
        return new File("target/sonar-data/" + testName + "/" + getResource(classKey));
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String json;
        try {
            String resourceKey = request.getParameter("resource");
            String[] parts = resourceKey.split(":");
            String groupId = parts[0];
            String artifactId = parts[1];
            String classKey = parts[2];
            SonarTestServer.LOG.debug("Loading data for {}:{}:{}", new Object[]{groupId, artifactId, classKey});
            if (classKey.startsWith(DEFAULT_PACKAGE_NAME)) {
                classKey = StringUtils.substringAfter(classKey, ".");
            }
            String testName = StringUtils.substringAfterLast(groupId, ".");

            json = FileUtils.readFileToString(getResourceAsFile(testName, classKey));
        } catch (Exception e) {
            json = "[]";
        }
        out.println(json);
    }
}
