/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Sonar-IDE is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar-IDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar-IDE; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.idea.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.psi.PsiFile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sonar.wsclient.Sonar;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

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
    Assert.assertTrue(Pattern.matches("[a-zA-Z_0-9.]+", inspectionTool.getShortName())); // see SONARIDE-57
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
