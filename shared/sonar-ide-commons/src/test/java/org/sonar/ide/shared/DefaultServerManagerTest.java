package org.sonar.ide.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;
import org.sonar.ide.api.SonarIdeException;
import org.sonar.ide.shared.DefaultServerManager.IServerSetListener;
import org.sonar.wsclient.Host;

/**
 * @author Jérémie Lagarde
 */
public class DefaultServerManagerTest {

  private static final String SERVER_CACHE_NAME = ".serverlist"; //$NON-NLS-1$

  private String              path;

  @Before
  public void init() throws Exception {
    path = System.getProperty("java.io.tmpdir");
    File file = new File(path + File.separator + SERVER_CACHE_NAME);
    if (file.exists())
      file.delete();
  }

  @Test
  public void testAddServerString() throws Exception {
    DefaultServerManager serverManager = new DefaultServerManager(path);
    serverManager.addServer("http://localhost:9000/", null, null);
    List<String> result = read();
    assertEquals(1, result.size());
    assertEquals("http://localhost:9000/|null|null", result.get(0));
  }

  @Test
  public void testAddServerStringStringString() throws Exception {
    DefaultServerManager serverManager = new DefaultServerManager(path);
    serverManager.addServer("http://localhost:9000/", "jeremie", "test");
    List<String> result = read();
    assertEquals(1, result.size());
    assertEquals("http://localhost:9000/|jeremie|test", result.get(0));
  }

  @Test
  public void testAddServerHost() throws Exception {
    DefaultServerManager serverManager = new DefaultServerManager(path);
    Host host = new Host("http://localhost:9000/");
    serverManager.addServer(host);
    List<String> result = read();
    assertEquals(1, result.size());
    assertEquals("http://localhost:9000/|null|null", result.get(0));
  }

  @Test
  public void testAddServerHost2() throws Exception {
    DefaultServerManager serverManager = new DefaultServerManager(path);
    Host host = new Host("http://localhost:9000/", "jeremie", "test");
    serverManager.addServer(host);
    List<String> result = read();
    assertEquals(1, result.size());
    assertEquals("http://localhost:9000/|jeremie|test", result.get(0));
  }

  @Test
  public void testAddServerMulti() throws Exception {
    DefaultServerManager serverManager = new DefaultServerManager(path);
    for (int i = 0; i < 5; i++)
      serverManager.addServer("http://localhost:900" + i, "jeremie", "test");
    List<String> result = read();
    assertEquals(5, result.size());
    for (int i = 0; i < 5; i++)
      assertEquals("http://localhost:900" + i + "|jeremie|test", result.get(i));
  }

  @Test
  public void testAddServerDuplicate() throws Exception {
    DefaultServerManager serverManager = new DefaultServerManager(path);
    try {
      serverManager.addServer("http://localhost:9000", "jeremie", "test");
      serverManager.addServer("http://localhost:9000", "", "");
      fail("Duplicated server!");
    } catch (SonarIdeException e) {
      assertEquals("Duplicate server: http://localhost:9000", e.getMessage());
    }
  }

  @Test
  public void testFindServer() throws Exception {
    List<String> lines = new ArrayList<String>();
    for (int i = 0; i < 5; i++)
      lines.add("http://localhost:900" + i + "|jeremie" + i + "|test" + i);
    write(lines);
    DefaultServerManager serverManager = new DefaultServerManager(path);
    Host host = serverManager.findServer("http://localhost:9002");
    assertEquals("http://localhost:9002", host.getHost());
    assertEquals("jeremie2", host.getUsername());
    assertEquals("test2", host.getPassword());
  }

  @Test
  public void testRemoveServer() {
    List<String> lines = new ArrayList<String>();
    for (int i = 0; i < 5; i++)
      lines.add("http://localhost:900" + i + "|jeremie" + i + "|test" + i);
    write(lines);
    DefaultServerManager serverManager = new DefaultServerManager(path);
    Host host = serverManager.findServer("http://localhost:9002");
    assertNotNull(host);
    serverManager.removeServer("http://localhost:9002");
    host = serverManager.findServer("http://localhost:9002");
    assertNull(host);
  }

  @Test
  public void testIServerSetListenerAdd() throws Exception {
    DefaultServerManager serverManager = new DefaultServerManager(path);
    final AtomicLong count = new AtomicLong(0);
    for (int i = 0; i < 3; i++) {
      serverManager.addServerSetListener(new IServerSetListener() {
        public void serverSetChanged(int type, List<Host> serverList) {
          if (IServerSetListener.SERVER_ADDED == type)
            count.incrementAndGet();
        }
      });
    }
    serverManager.addServer("http://localhost:9000/", null, null);
    serverManager.addServer("http://nemo.sonarsource.org", null, null);
    assertEquals(6, count.get());
  }

  @Test
  public void testIServerSetListenerRemove() throws Exception {
    DefaultServerManager serverManager = new DefaultServerManager(path);
    final AtomicLong count = new AtomicLong(0);
    for (int i = 0; i < 3; i++) {
      serverManager.addServerSetListener(new IServerSetListener() {
        public void serverSetChanged(int type, List<Host> serverList) {
          if (IServerSetListener.SERVER_REMOVED == type)
            count.incrementAndGet();
        }
      });
    }
    serverManager.addServer("http://localhost:9000/", null, null);
    serverManager.addServer("http://nemo.sonarsource.org", null, null);
    serverManager.removeServer("http://localhost:9000/");
    assertEquals(3, count.get());
  }

  protected List<String> read() throws Exception {
    File file = new File(path + File.separator + SERVER_CACHE_NAME);
    if (!file.exists()) {
      fail(path + File.separator + SERVER_CACHE_NAME + " dosen't exist!");
    }
    FileInputStream fis = null;
    BufferedReader reader = null;
    List<String> lines = new ArrayList<String>();
    try {
      fis = new FileInputStream(file);
      reader = new BufferedReader(new InputStreamReader(fis));
      String line = null;
      do {
        line = reader.readLine();
        if (line != null && line.trim().length() > 0) {
          lines.add(line);
        }
      } while (line != null);
    } finally {
      if (fis != null) {
        fis.close();
      }
      if (reader != null) {
        reader.close();
      }
    }
    return lines;
  }

  protected void write(List<String> lines) {
    File file = new File(path + File.separator + SERVER_CACHE_NAME);
    if (file.exists()) {
      fail(path + File.separator + SERVER_CACHE_NAME + " exist!");
    }
    FileOutputStream fos = null;
    PrintWriter writer = null;
    try {
      fos = new FileOutputStream(file);
      writer = new PrintWriter(fos);
      for (String line : lines) {
        writer.println(line);
      }
      writer.flush();
      fos.flush();
    } catch (Exception ex) {
      fail(ex.getMessage());
    } finally {
      if (writer != null) {
        writer.close();
      }
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException ex) {
          fail(ex.getMessage());
        }
      }
    }
  }

}
