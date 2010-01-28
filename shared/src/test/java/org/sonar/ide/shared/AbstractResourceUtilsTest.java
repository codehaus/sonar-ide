package org.sonar.ide.shared;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Evgeny Mandrikov
 */
public class AbstractResourceUtilsTest {
  class FileModel {
  }

  abstract class ResourceUtils extends AbstractResourceUtils<FileModel> {
  }

  @Test
  public void testDefaultPackage() throws Exception {
    ResourceUtils mock = createMock("test:test", "", "ClassOnDefaultPackage");
    Assert.assertEquals("test:test:[default]:ClassOnDefaultPackage", mock.getResourceKey(new FileModel()));

    mock = createMock("test:test", null, "ClassOnDefaultPackage");
    Assert.assertEquals("test:test:[default]:ClassOnDefaultPackage", mock.getResourceKey(new FileModel()));
  }

  @Test
  public void testSimpleClass() throws Exception {
    ResourceUtils mock = createMock("test:test", "org.sonar", "ClassOne");
    Assert.assertEquals("test:test:org.sonar:ClassOne", mock.getResourceKey(new FileModel()));
  }

  private ResourceUtils createMock(String projectKey, String packageKey, String fileKey) {
    ResourceUtils mock = mock(ResourceUtils.class);
    when(mock.getFileName((FileModel) any())).thenReturn(fileKey);
    when(mock.getPackageName((FileModel) any())).thenReturn(packageKey);
    when(mock.getProjectKey((FileModel) any())).thenReturn(projectKey);
    return mock;
  }
}
