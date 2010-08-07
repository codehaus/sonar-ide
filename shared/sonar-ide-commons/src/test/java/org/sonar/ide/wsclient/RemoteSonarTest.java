package org.sonar.ide.wsclient;

import org.junit.Before;
import org.junit.Test;
import org.sonar.ide.api.SourceCode;
import org.sonar.ide.test.SonarIdeTestCase;
import org.sonar.wsclient.Host;

/**
 * @author Evgeny Mandrikov
 */
public class RemoteSonarTest extends SonarIdeTestCase {

  private RemoteSonar remoteSonar;

  @Before
  public void setUp() throws Exception {
    remoteSonar = new RemoteSonar(new Host("http://nemo.sonarsource.org"));
  }

  @Test
  public void test() throws Exception {
    SourceCode sourceCode;

    sourceCode = remoteSonar.search("org.codehaus.sonar:sonar-squid:org.sonar.squid");
    System.out.println("Measures for package: " + sourceCode.getMeasures());
    System.out.println("Children for package: " + sourceCode.getChildren());

    sourceCode = remoteSonar.search("org.codehaus.sonar:sonar-squid:org.sonar.squid.api.SourceCode");
    System.out.println(sourceCode);
    System.out.println(sourceCode.getMeasures());
    System.out.println(sourceCode.getViolations());
    System.out.println(sourceCode.getDuplications());
    System.out.println(sourceCode.getCoverage());
  }

}
