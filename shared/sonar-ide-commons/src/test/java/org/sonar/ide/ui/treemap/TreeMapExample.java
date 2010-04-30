/*
 * Copyright (C) 2010 Evgeny Mandrikov
 *
 * Sonar-IDE is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar-IDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar-IDE; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.ide.ui.treemap;

import org.sonar.ide.ui.SwingAppRunner;
import org.sonar.ide.ui.treemap.split.SplitByWeight;

/**
 * @author Evgeny Mandrikov
 */
public class TreeMapExample {
  protected static class Resource {
    final String label;
    final double size;
    final double coverage;

    public Resource(double size, String label) {
      this(size, 0, label);
    }

    public Resource(double size, double coverage, String label) {
      this.size = size;
      this.coverage = coverage;
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
    }
  }

  protected static class ResourceColorProvider extends AbstractColorProvider<Resource> {
    protected ResourceColorProvider() {
      super(0, 100);
    }

    @Override
    protected float getColorValue(Resource value) {
      return (float) value.coverage;
    }
  }

  protected static class ResourceTooltipBuilder implements TooltipProvider<Resource> {
    public String getToolTipText(Resource resource) {
      return new StringBuilder()
          .append("<html><table>")
          .append("<tr><td colspan='2'><b>").append(resource.label).append("</b><td/></tr>")
          .append("<tr><td>Lines of code</td><td><b>").append(resource.size).append("</b></td></tr>")
          .append("<tr><td>Coverage</td><td><b>").append(resource.coverage).append("</b></td></tr>")
          .append("</table></html>")
          .toString();
    }
  }

  public static TreeMapNode<Resource> build(double weight, String label) {
    return new TreeMapNode<Resource>(weight, new Resource(weight, label));
  }

  public static TreeMapNode<Resource> build(double weight, double coverage, String label) {
    return new TreeMapNode<Resource>(weight, new Resource(weight, coverage, label));
  }

  public static TreeMapNode<Resource> build() {
    TreeMapNode<Resource> root = new TreeMapNode<Resource>();
    root.add(build(60, 90, "IconsUtils"));
    root.add(build(47, "MeasuresViewer"));
    root.add(build(39, "AbstractViewer"));
    root.add(build(31, "Demo"));
    root.add(build(28, "SonarConfigPanel"));
    root.add(build(15, "TabbetPaneBuilder"));
    root.add(build(14, "AbstractConfigPanel"));
    root.add(build(13, "DefaultIconLoader"));
    root.add(build(5, "AbstractIconLoader"));
    /*
    root.add(build(400, 75.0, "org.sonar.ide.shared"));
    root.add(build(252, 24.5, "org.sonar.ide.ui"));
    root.add(build(46, 0, "org.sonar.ide.client"));
    */
    return root;
  }

  public static void main(String[] args) {
    JTreeMap<Resource> treeMap = new JTreeMap<Resource>(build())
        .setStrategy(new SplitByWeight())
        .setColorProvider(new ResourceColorProvider())
        .setTooltipProvider(new ResourceTooltipBuilder());
    SwingAppRunner.run(treeMap);
  }
}
