package org.sonar.ide.client;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sonar.ide.test.SonarTestServer;

import static org.hamcrest.CoreMatchers.is;

/**
 * @author Evgeny Mandrikov
 */
public class SonarClientTest {
    private SonarTestServer testServer;
    private SonarClient client;

    @Before
    public void setUp() throws Exception {
        testServer = new SonarTestServer();
        testServer.start();

        client = new SonarClient(testServer.getBaseUrl());
    }

    @After
    public void tearDown() throws Exception {
        testServer.stop();
    }

    @Test
    public void testCheckVersion() throws Exception {
        Assert.assertThat(client.isAvailable(), is(true));
    }
}
