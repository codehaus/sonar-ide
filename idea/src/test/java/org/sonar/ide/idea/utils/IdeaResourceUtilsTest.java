package org.sonar.ide.idea.utils;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import org.mockito.Mockito;
import org.sonar.ide.shared.AbstractResourceUtilsTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Evgeny Mandrikov
 */
public class IdeaResourceUtilsTest extends AbstractResourceUtilsTest<PsiFile> {
  @Override
  protected PsiFile newFileModel(boolean java, String projectKey, String packageOrDirectory, String fileName) {
    if (java) {
      PsiJavaFile psiJavaFile = mock(PsiJavaFile.class);
      when(psiJavaFile.getName()).thenReturn(fileName);
      when(psiJavaFile.getPackageName()).thenReturn(packageOrDirectory);
      return psiJavaFile;
    } else {
      PsiFile psiFile = mock(PsiFile.class);
      when(psiFile.getName()).thenReturn(fileName);
      return psiFile;
    }
  }

  @Override
  protected IdeaResourceUtils newUtils(boolean java, String projectKey, String packageOrDirectory, String fileName) {
    IdeaResourceUtils utils = Mockito.spy(IdeaResourceUtils.getInstance());
    Mockito.doReturn(projectKey).when(utils).getProjectKey(Mockito.<PsiFile>any());
    Mockito.doReturn(packageOrDirectory).when(utils).getDirectoryPath(Mockito.<PsiFile>any());
    return utils;
  }
}
