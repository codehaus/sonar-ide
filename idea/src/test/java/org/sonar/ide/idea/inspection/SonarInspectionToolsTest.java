package org.sonar.ide.idea.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.psi.PsiFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sonar.wsclient.Sonar;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author Evgeny Mandrikov
 */
@RunWith(Parameterized.class)
public class SonarInspectionToolsTest {
  private AbstractSonarInspectionTool inspectionTool;

  public SonarInspectionToolsTest(AbstractSonarInspectionTool inspectionTool) {
    this.inspectionTool = inspectionTool;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        {new Violations()},
        {new Duplications()},
        {new Dependencies()}
    });
  }

  @Test
  public void testNames() {
    assertThat(inspectionTool.getGroupDisplayName(), is("Sonar"));
    assertThat(inspectionTool.getDisplayName(), notNullValue());
    assertThat(inspectionTool.getShortName(), notNullValue());
    assertThat(inspectionTool.getStaticDescription(), notNullValue());
  }

  @Test
  public void shouldBeEnabledByDefault() {
    assertThat(inspectionTool.isEnabledByDefault(), is(true));
  }

  @Test
  public void shouldSkipOnTheFly() {
    PsiFile psiFile = mock(PsiFile.class);
    InspectionManager inspectionManager = mock(InspectionManager.class);
    Sonar sonar = mock(Sonar.class);
    assertThat(inspectionTool.checkFileBySonar(psiFile, inspectionManager, true, sonar), nullValue());
  }
}
