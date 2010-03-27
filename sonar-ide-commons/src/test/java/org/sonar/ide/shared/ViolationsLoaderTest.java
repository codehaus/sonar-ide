package org.sonar.ide.shared;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.sonar.ide.test.AbstractSonarIdeTest;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.ConnectionException;
import org.sonar.wsclient.services.Violation;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class ViolationsLoaderTest extends AbstractSonarIdeTest {
  @Test
  public void testGetHashCode() {
    int hash1 = ViolationsLoader.getHashCode("int i;");
    int hash2 = ViolationsLoader.getHashCode("int\ti;");
    int hash3 = ViolationsLoader.getHashCode("int i;\n");
    int hash4 = ViolationsLoader.getHashCode("int i;\r\n");
    int hash5 = ViolationsLoader.getHashCode("int i;\r");

    assertThat(hash2, equalTo(hash1));
    assertThat(hash3, equalTo(hash1));
    assertThat(hash4, equalTo(hash1));
    assertThat(hash5, equalTo(hash1));
  }

  @Test(expected = ConnectionException.class)
  public void testServerUnavailable() throws Exception {
    ViolationsLoader.getViolations(Sonar.create("http://localhost:9999"), "test:test:[default].ClassOnDefaultPackage", "");
  }

  @Test
  public void testGetViolations() throws Exception {
    init(); // TODO remove from here
    File project = getProject("SimpleProject");

    List<Violation> violations = ViolationsLoader.getViolations(
        getTestServer().getSonar(),
        getProjectKey(project) + ":[default].ClassOnDefaultPackage",
        FileUtils.readFileToString(getProjectFile(project, "/src/main/java/ClassOnDefaultPackage.java"))
    );

    assertThat(violations.size(), is(4));
    // TODO assert lines
  }

  /**
   * See <a href="http://jira.codehaus.org/browse/SONARIDE-52">SONARIDE-52</a>
   */
  @Test
  public void testViolationOnFile() throws Exception {
    init(); // TODO remove from here
    File project = getProject("SimpleProject");

    List<Violation> violations = ViolationsLoader.getViolations(
        getTestServer().getSonar(),
        getProjectKey(project) + ":[default].ViolationOnFile",
        FileUtils.readFileToString(getProjectFile(project, "/src/main/java/ViolationOnFile.java"))
    );

    assertThat(violations.size(), is(0));
  }

  /**
   * See <a href="http://jira.codehaus.org/browse/SONARIDE-13">SONARIDE-13</a>
   */
  @Test
  public void testCodeChanged() throws Exception {
    init(); // TODO remove from here
    File project = getProject("SimpleProject");

    List<Violation> violations = ViolationsLoader.getViolations(
        getTestServer().getSonar(),
        getProjectKey(project) + ":[default].CodeChanged",
        FileUtils.readFileToString(getProjectFile(project, "/src/main/java/CodeChanged.java"))
    );

    assertThat(violations.size(), is(1));
    assertThat(violations.get(0).getLine(), is(4));
  }

  @Test
  public void testLineForViolationDoesntExists() throws Exception {
    init(); // TODO remove from here
    File project = getProject("SimpleProject");

    List<Violation> violations = ViolationsLoader.getViolations(
        getTestServer().getSonar(),
        getProjectKey(project) + ":[default].LineForViolationDoesntExists",
        FileUtils.readFileToString(getProjectFile(project, "/src/main/java/LineForViolationDoesntExists.java"))
    );

    assertThat(violations.size(), is(1));
    assertThat(violations.get(0).getLine(), is(2));
  }
}
