package org.sonar.ide.idea.utils;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import org.junit.Ignore;
import org.mockito.Mockito;
import org.sonar.ide.shared.AbstractResourceUtilsTest;

import static org.mockito.Mockito.mock;

/**
 * @author Evgeny Mandrikov
 */
@Ignore("Not ready")
public class IdeaResourceUtilsTest extends AbstractResourceUtilsTest<PsiFile> {
  public IdeaResourceUtilsTest() {
    super(getMock());
  }

  @Override
  protected PsiFile newFileModel(boolean java, String projectKey, String packageOrDirectory, String fileName) {
    return java ?
        mock(PsiJavaFile.class) :
        mock(PsiFile.class);
  }

  private static IdeaResourceUtils getMock() {
    IdeaResourceUtils utils = Mockito.spy(IdeaResourceUtils.getInstance());
    Mockito.doReturn(null).when(utils).getMavenProject(Mockito.<PsiFile>any());
    return utils;
  }
}
