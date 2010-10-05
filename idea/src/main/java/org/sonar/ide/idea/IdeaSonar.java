/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.idea;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiFile;
import org.sonar.ide.api.SourceCode;
import org.sonar.ide.idea.utils.IdeaResourceUtils;
import org.sonar.ide.wsclient.RemoteSonar;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;

/**
 * @author Evgeny Mandrikov
 */
public class IdeaSonar extends RemoteSonar {

  public IdeaSonar(Host host) {
    super(host);
  }

  /**
   * For IntelliJ IDEA use {@link #search(com.intellij.psi.PsiFile)} instead of it.
   * {@inheritDoc}
   */
  @Override
  public SourceCode search(String key) {
    return super.search(key);
  }

  public SourceCode search(final PsiFile file) {
    return ApplicationManager.getApplication().runReadAction(new Computable<SourceCode>() {
      @Override
      public SourceCode compute() {
        return search(IdeaResourceUtils.getInstance().getFileKey(file))
            .setLocalContent(file.getText());
      }
    });
  }

}
