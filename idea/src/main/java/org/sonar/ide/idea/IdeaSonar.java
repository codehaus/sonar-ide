package org.sonar.ide.idea;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiFile;
import org.sonar.ide.api.SourceCode;
import org.sonar.ide.idea.utils.IdeaResourceUtils;
import org.sonar.ide.wsclient.RemoteSonar;
import org.sonar.wsclient.Sonar;

/**
 * @author Evgeny Mandrikov
 */
public class IdeaSonar extends RemoteSonar {

  public IdeaSonar(Sonar sonar) {
    super(sonar);
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
