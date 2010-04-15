package org.sonar.ide.shared;

import org.junit.Test;
import org.sonar.wsclient.Host;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author Evgeny Mandrikov
 */
public class SonarPropertiesTest {
    @Test
    public void testStaticMethods() {
        assertNotNull(SonarProperties.getDefaultPath());
        assertNotNull(SonarProperties.getInstance());
    }

    @Test
    public void testDefaultProperties() throws Exception {
        SonarProperties properties = new SonarProperties(null);

        Host server = properties.getServer();
        assertEquals(SonarProperties.HOST_DEFAULT, server.getHost());
        assertNull(server.getUsername());
        assertNull(server.getPassword());
    }

    @Test
    public void testSave() throws Exception {
        File file = File.createTempFile("tmp", "", new File(System.getProperty("java.io.tmpdir")));
        SonarProperties properties = new SonarProperties(file.getPath());
        properties.save();
    }
}
