package org.sonar.ide.shared;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
    assertEquals(PROJECT_KEY + ":[default]." + CLASS_NAME, mock.getResourceKey(new FileModel()));

    mock = createMock(PROJECT_KEY, null, CLASS_NAME);
    assertEquals(PROJECT_KEY + ":[default]." + CLASS_NAME, mock.getResourceKey(new FileModel()));
  }

  @Test
  public void testSimpleClass() throws Exception {
    ResourceUtils mock = createMock(PROJECT_KEY, PACKAGE_NAME, CLASS_NAME);
    assertEquals(PROJECT_KEY + ":" + PACKAGE_NAME + "." + CLASS_NAME, mock.getResourceKey(new FileModel()));
  }

  private ResourceUtils createMock(String projectKey, String packageKey, String fileKey) {
    ResourceUtils mock = mock(ResourceUtils.class);
    when(mock.getFileName((FileModel) any())).thenReturn(fileKey);
    when(mock.getPackageName((FileModel) any())).thenReturn(packageKey);
    when(mock.getProjectKey((FileModel) any())).thenReturn(projectKey);
    return mock;
  }
}
