package org.sonar.ide.idea;

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
    super(sonar, null); // TODO diffEngine
  }

  public SourceCode search(PsiFile file) {
    return search(IdeaResourceUtils.getInstance().getFileKey(file));
  }

}
