package org.sonar.ide.ui.treemap;

import org.sonar.ide.ui.treemap.split.SplitByWeight;

import javax.swing.*;
import java.awt.*;

/**
 * @author Evgeny Mandrikov
 */
public class TreeMapExample {
  protected static class Resource {
    final Color color;
    final String label;
    final double size;
    final double coverage;

    public Resource(double size, String label) {
      this(size, 0, label, Color.RED);
    }

    public Resource(double size, double coverage, String label, Color color) {
      this.size = size;
      this.coverage = coverage;
      this.label = label;
      this.color = color;
    }

    @Override
    public String toString() {
      return label;
    }
  }

  protected static class ResourceColorProvider implements ColorProvider<Resource> {
    public Color getColor(Resource value) {
      return value.color;
    }
  }

  protected static class ResourceTooltipBuilder implements TooltipProvider<Resource> {
    public String getToolTipText(Resource resource) {
      return "<html><b>" + resource.label + "</b><br/>" +
          "Lines of code <b>" + resource.size + "</b><br/>" +
          "Coverage <b>" + resource.coverage + "</b>";
    }
  }

  public static TreeMapNode<Resource> build(double weight, String label) {
    return new TreeMapNode<Resource>(weight, new Resource(weight, label));
  }

  public static TreeMapNode<Resource> build(double weight, double coverage, String label, Color color) {
    return new TreeMapNode<Resource>(weight, new Resource(weight, coverage, label, color));
  }

  public static TreeMapNode<Resource> build() {
    TreeMapNode<Resource> root = new TreeMapNode<Resource>();
    root.add(build(60, 90, "IconsUtils", Color.GREEN));
    root.add(build(47, "MeasuresViewer"));
    root.add(build(39, "AbstractViewer"));
    root.add(build(31, "Demo"));
    root.add(build(28, "SonarConfigPanel"));
    root.add(build(15, "TabbetPaneBuilder"));
    root.add(build(14, "AbstractConfigPanel"));
    root.add(build(13, "DefaultIconLoader"));
    root.add(build(5, "AbstractIconLoader"));
    return root;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

  private static void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("SonarIDE Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //Create and set up the content pane.
    JTreeMap<Resource> treeMap = new JTreeMap<Resource>(build())
        .setStrategy(new SplitByWeight())
        .setColorProvider(new ResourceColorProvider())
        .setTooltipProvider(new ResourceTooltipBuilder());
    frame.setContentPane(treeMap);
    //Display the window.
    frame.setSize(400, 400);
    frame.setVisible(true);
  }
}
