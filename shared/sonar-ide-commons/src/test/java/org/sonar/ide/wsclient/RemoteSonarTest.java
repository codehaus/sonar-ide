package org.sonar.ide.wsclient;

import org.junit.Before;
import org.junit.Test;
import org.sonar.ide.api.SourceCode;
import org.sonar.ide.test.SonarIdeTestCase;
import org.sonar.wsclient.Sonar;

/**
 * @author Evgeny Mandrikov
 */
public class RemoteSonarTest extends SonarIdeTestCase {

  private RemoteSonar remoteSonar;

  @Before
  public void setUp() throws Exception {
//    SonarTestServer server = getTestServer();
//    Sonar sonar = server.getSonar()
    Sonar sonar = Sonar.create("http://nemo.sonarsource.org");
    remoteSonar = new RemoteSonar(sonar);
  }

  @Test
  public void test() throws Exception {
    SourceCode sourceCode;

    sourceCode = remoteSonar.search("org.codehaus.sonar:sonar-squid:org.sonar.squid.api");
    System.out.println(sourceCode.getMeasures());

    sourceCode = remoteSonar.search("org.codehaus.sonar:sonar-squid:org.sonar.squid.api.SourceCode");
    System.out.println(sourceCode);
    System.out.println(sourceCode.getMeasures());
    System.out.println(sourceCode.getViolations());
    System.out.println(sourceCode.getDuplications());
    System.out.println(sourceCode.getCoverage());
    System.out.println(sourceCode.getCode());
  }

}
