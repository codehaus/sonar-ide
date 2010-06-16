package org.sonar.ide.wsclient;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class SimpleSourceCodeDiffEngineTest {

  @Test
  public void testGetHashCode() {
    int hash1 = SimpleSourceCodeDiffEngine.getHashCode("int i;");
    int hash2 = SimpleSourceCodeDiffEngine.getHashCode("int\ti;");
    int hash3 = SimpleSourceCodeDiffEngine.getHashCode("int i;\n");
    int hash4 = SimpleSourceCodeDiffEngine.getHashCode("int i;\r\n");
    int hash5 = SimpleSourceCodeDiffEngine.getHashCode("int i;\r");

    assertThat(hash2, equalTo(hash1));
    assertThat(hash3, equalTo(hash1));
    assertThat(hash4, equalTo(hash1));
    assertThat(hash5, equalTo(hash1));
  }

  @Test
  public void testSplit() {
    assertThat(SimpleSourceCodeDiffEngine.split("\ntest\n").length, is(3));
  }

}
