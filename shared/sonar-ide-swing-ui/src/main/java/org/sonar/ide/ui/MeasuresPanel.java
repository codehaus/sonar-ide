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

package org.sonar.ide.ui;

import org.sonar.ide.shared.MetricUtils;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Metric;
import org.sonar.wsclient.services.Resource;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Evgeny Mandrikov
 */
public class MeasuresPanel extends JPanel {
  private JTree tree;

  public MeasuresPanel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    tree = new JTree(new DefaultMutableTreeNode("Measures"));
    tree.setRootVisible(false);
    add(new JScrollPane(tree));
  }

  public void load(List<Metric> metrics, Resource resource) {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Measures");
    Map<String, java.util.List<Metric>> domains = MetricUtils.splitByDomain(metrics);

    for (Map.Entry<String, List<Metric>> entry : domains.entrySet()) {
      DefaultMutableTreeNode domainNode = new DefaultMutableTreeNode(entry.getKey());
      root.add(domainNode);
      for (Metric metric : entry.getValue()) {
        Measure measure = resource.getMeasure(metric.getKey());
        domainNode.add(new DefaultMutableTreeNode(measure.getMetricName() + " = " + measure.getFormattedValue()));
      }
    }

    tree.setModel(new DefaultTreeModel(root, false));
  }

  public static void main(String[] args) {
    MeasuresPanel panel = new MeasuresPanel();
    List<Metric> metrics = Arrays.asList(
        new Metric().setDomain("Size").setKey("accessors"),
        new Metric().setDomain("Size").setKey("classes"),
        new Metric().setDomain("Duplications").setKey("duplicated_files")
    );
    Resource resource = new Resource();
    resource.setMeasures(Arrays.asList(
        new Measure().setMetricKey("accessors").setMetricName("Accessors").setFormattedValue("3,400"),
        new Measure().setMetricKey("classes").setMetricName("Classes").setFormattedValue("56"),
        new Measure().setMetricKey("duplicated_files").setMetricName("Duplicated files").setFormattedValue("4")
    ));
    panel.load(metrics, resource);
    SwingAppRunner.run(panel);
  }
}
