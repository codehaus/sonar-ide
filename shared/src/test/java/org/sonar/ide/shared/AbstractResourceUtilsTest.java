package org.sonar.ide.shared;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Evgeny Mandrikov
 */
public class AbstractResourceUtilsTest {
  private static final String PROJECT_KEY = "test:test";
  private static final String CLASS_NAME = "ClassOne";
  private static final String PACKAGE_NAME = "org.sonar";
  private static final String COMMON_CLASS = PROJECT_KEY + ":" + PACKAGE_NAME + "." + CLASS_NAME;
  private static final String CLASS_ON_DEFAULT_PACKAGE = PROJECT_KEY + ":[default]." + CLASS_NAME;

  class FileModel {
  }

  abstract class ResourceUtils extends AbstractResourceUtils<FileModel> {
  }

  @Test
  public void testNoMavenProject() {
    ResourceUtils mock = createMock(null, PACKAGE_NAME, CLASS_NAME);
    assertNull(mock.getResourceKey(new FileModel()));
  }

  @Test
  public void testDefaultPackage() throws Exception {
    ResourceUtils mock = createMock(PROJECT_KEY, "", CLASS_NAME);
    assertThat(
        mock.getResourceKey(new FileModel()),
        is(CLASS_ON_DEFAULT_PACKAGE)
    );

    mock = createMock(PROJECT_KEY, null, CLASS_NAME);
    assertThat(
        mock.getResourceKey(new FileModel()),
        is(CLASS_ON_DEFAULT_PACKAGE)
    );
  }

  @Test
  public void testSimpleClass() throws Exception {
    ResourceUtils mock = createMock(PROJECT_KEY, PACKAGE_NAME, CLASS_NAME);
    assertThat(
        mock.getResourceKey(new FileModel()),
        is(COMMON_CLASS)
    );
  }

  private ResourceUtils createMock(String projectKey, String packageKey, String fileKey) {
    ResourceUtils mock = mock(ResourceUtils.class);
    when(mock.getFileName((FileModel) any())).thenReturn(fileKey);
    when(mock.getPackageName((FileModel) any())).thenReturn(packageKey);
    when(mock.getProjectKey((FileModel) any())).thenReturn(projectKey);
    return mock;
  }
}
