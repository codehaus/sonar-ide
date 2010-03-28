package org.sonar.ide.shared;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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
  private static File project;

  @BeforeClass
  public static void init() throws Exception {
    AbstractSonarIdeTest.init();
    project = getProject("SimpleProject");
  }

  @AfterClass
  public static void cleanup() throws Exception {
    AbstractSonarIdeTest.cleanup();
  }

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

  private List<Violation> getViolations(String className) throws Exception {
    return ViolationsLoader.getViolations(
        getTestServer().getSonar(),
        getProjectKey(project) + ":[default]." + className,
        FileUtils.readFileToString(getProjectFile(project, "/src/main/java/" + className + ".java"))
    );
  }

  @Test
  public void testGetViolations() throws Exception {
    List<Violation> violations = getViolations("ClassOnDefaultPackage");

    assertThat(violations.size(), is(4));
    // TODO assert lines
  }

  /**
   * See <a href="http://jira.codehaus.org/browse/SONARIDE-52">SONARIDE-52</a>
   */
  @Test
  public void testViolationOnFile() throws Exception {
    List<Violation> violations = getViolations("ViolationOnFile");

    assertThat(violations.size(), is(0));
  }

  /**
   * See <a href="http://jira.codehaus.org/browse/SONARIDE-13">SONARIDE-13</a>
   */
  @Test
  public void testCodeChanged() throws Exception {
    List<Violation> violations = getViolations("CodeChanged");

    assertThat(violations.size(), is(1));
    assertThat(violations.get(0).getLine(), is(4));
  }

  @Test
  public void testLineForViolationDoesntExists() throws Exception {
    List<Violation> violations = getViolations("LineForViolationDoesntExists");

    assertThat(violations.size(), is(1));
    assertThat(violations.get(0).getLine(), is(2));
  }

  @Test
  public void testMoreThanOneMatch() throws Exception {
    List<Violation> violations = getViolations("MoreThanOneMatch");

    assertThat(violations.size(), is(2));
    assertThat(violations.get(0).getLine(), is(4));
    assertThat(violations.get(1).getLine(), is(4));
  }
}
