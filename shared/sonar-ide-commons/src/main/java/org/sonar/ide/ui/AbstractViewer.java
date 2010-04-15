package org.sonar.ide.ui;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import javax.swing.*;

/**
 * TODO actually in Sonar such class called "ViewerHeader"
 *
 * @author Evgeny Mandrikov
 */
public abstract class AbstractViewer extends JPanel {
  protected final String[] metrics;
  private AbstractIconLoader iconLoader;

  protected AbstractViewer(Sonar sonar, AbstractIconLoader iconLoader, String resourceKey, String... metrics) {
    super();
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.metrics = metrics;
    this.iconLoader = iconLoader;
    loadMeasures(sonar, resourceKey);
  }

  public abstract String getTitle();

  private void loadMeasures(Sonar sonar, String resourceKey) {
    ResourceQuery query = ResourceQuery.createForMetrics(resourceKey, metrics)
        .setIncludeTrends(true)
        .setVerbose(true);
    display(sonar.find(query));
  }

  protected abstract void display(Resource resource);

  protected void addCell(Measure... measures) {
    JPanel cell = new JPanel();
    cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
    if (measures != null) {
      for (Measure measure : measures) {
        if (measure != null && measure.getFormattedValue() != null) {
          JLabel label = new JLabel("<html><b>" + measure.getMetricName() + ":</b> " + measure.getFormattedValue() + "</html>");
          label.setIcon(iconLoader.getIcon(IconsUtils.getTendencyIconPath(measure)));
          cell.add(label);
        }
      }
    }
    add(cell);
  }
}
