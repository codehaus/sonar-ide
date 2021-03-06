/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.netbeans.tasklist;

import org.netbeans.spi.tasklist.FileTaskScanner;
import org.netbeans.spi.tasklist.Task;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;
import org.sonar.ide.netbeans.utils.NetbeansResourceUtils;
import org.sonar.ide.shared.SonarProperties;
import org.sonar.ide.shared.ViolationUtils;
import org.sonar.ide.shared.ViolationsLoader;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.Violation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class SonarTaskScanner extends FileTaskScanner {
  private Callback callback;

  public SonarTaskScanner() {
    super(
        NbBundle.getMessage(SonarTaskScanner.class, "SonarTaskScanner.label"),
        NbBundle.getMessage(SonarTaskScanner.class, "SonarTaskScanner.hint"),
        "Advanced"
    );
  }

  @Override
  public List<? extends Task> scan(FileObject resource) {
    if (resource == null || !"java".equalsIgnoreCase(resource.getExt())) {
      return null;
    }

    String resourceKey = new NetbeansResourceUtils().getFileKey(resource);

    SonarProperties sonarProperties = SonarProperties.getInstance();
    Sonar sonar = new Sonar(new HttpClient4Connector(sonarProperties.getServer()));

    Collection<Violation> violations = ViolationsLoader.getViolations(sonar, resourceKey);

    ArrayList<Task> tasks = new ArrayList<Task>();
    for (Violation violation : violations) {
      tasks.add(Task.create(
          resource,
          "org-sonar-ide-netbeans-Task",
          ViolationUtils.getDescription(violation),
          violation.getLine()
      ));
    }

    return tasks;
  }

  @Override
  public void attach(Callback callback) {
    this.callback = callback;
  }
}
