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
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.sonar.wsclient.Sonar;

import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class Dependencies extends AbstractSonarInspectionTool {
  @Override
  protected List<ProblemDescriptor> checkFileBySonar(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly, Sonar sonar) {
    if (isOnTheFly) {
      return null;
    }
    // TODO
    throw new NotImplementedException();
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return "Dependencies";
  }

  @NotNull
  @Override
  public String getShortName() {
    return "Sonar.Dependencies"; // TODO
  }

  @Override
  public String getStaticDescription() {
    return "Dependencies from Sonar";
  }
}
