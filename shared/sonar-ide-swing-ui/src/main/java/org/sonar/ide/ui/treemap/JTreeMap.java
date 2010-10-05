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

package org.sonar.ide.ui.treemap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author Evgeny Mandrikov
 */
public class JTreeMap<MODEL> extends JComponent {
  private static final int INSET = 4;

  /**
   * Root of tree.
   */
  private TreeMapNode<MODEL> root;

  /**
   * Displayed root.
   */
  private TreeMapNode<MODEL> displayedRoot;

  /**
   * Active leaf.
   */
  private TreeMapNode<MODEL> activeLeaf;

  /**
   * Divide strategy.
   */
  private AbstractSplitStrategy strategy;

  /**
   * Tooltip provider.
   */
  private TooltipProvider<MODEL> tooltipProvider;

  /**
   * Color provider.
   */
  private ColorProvider<MODEL> colorProvider;

  /**
   * @param root root of this tree
   */
  public JTreeMap(TreeMapNode<MODEL> root) {
    super();
    setRoot(root);
    setToolTipText("");
    addMouseMotionListener(new MouseMotionHandler());
  }

  /**
   * Sets new root.
   *
   * @param root new root
   */
  public void setRoot(TreeMapNode<MODEL> root) {
    this.root = root;
    setDisplayedRoot(root);
  }

  /**
   * Sets split strategy.
   *
   * @param strategy new split strategy
   * @return this, for method chaining
   */
  public JTreeMap<MODEL> setStrategy(AbstractSplitStrategy strategy) {
    this.strategy = strategy;
    return this;
  }

  /**
   * Sets tooltip provider.
   *
   * @param tooltipProvider new tooltip provider
   * @return this, for method chaining
   */
  public JTreeMap<MODEL> setTooltipProvider(TooltipProvider<MODEL> tooltipProvider) {
    this.tooltipProvider = tooltipProvider;
    return this;
  }

  /**
   * Sets color provider.
   *
   * @param colorProvider new color provider
   * @return this, for method chaining
   */
  public JTreeMap<MODEL> setColorProvider(ColorProvider<MODEL> colorProvider) {
    this.colorProvider = colorProvider;
    return this;
  }

  /**
   * Sets displayed root.
   *
   * @param displayedRoot new dusplayed root
   */
  public void setDisplayedRoot(final TreeMapNode<MODEL> displayedRoot) {
    this.displayedRoot = displayedRoot;
  }

  /**
   * Sets active leaf.
   *
   * @param activeLeaf new active leaf
   */
  public void setActiveLeaf(TreeMapNode<MODEL> activeLeaf) {
    this.activeLeaf = activeLeaf;
    setToolTipText(tooltipProvider.getToolTipText(activeLeaf.getUserObject()));
  }

  /**
   * Returns active leaf.
   *
   * @return active leaf
   */
  public TreeMapNode<MODEL> getActiveLeaf() {
    return activeLeaf;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    final int width = getSize().width;
    final int height = getSize().height;
    final Insets insets = getInsets();
    root.setDimension(root.getX(), root.getY(), width - insets.left - insets.right, height - insets.top - insets.bottom);
    if (!root.equals(displayedRoot)) {
      displayedRoot.setDimension(displayedRoot.getX(), displayedRoot.getY(), width - insets.left - insets.right, height - insets.top - insets.bottom);
    }
    calculatePositions();
    if (displayedRoot.children().hasMoreElements()) {
      drawNode(g, displayedRoot);
      drawLabels(g, displayedRoot);
    }
  }

  @Override
  public Point getToolTipLocation(MouseEvent event) {
    if (this.activeLeaf != null) {
      return new Point(
          activeLeaf.getX() + INSET,
          activeLeaf.getY() + INSET
      );
    }
    return null;
  }

  /**
   * Calculates positions for displayed root.
   */
  private void calculatePositions() {
    strategy.calculatePositions(displayedRoot);
  }

  private void drawNode(Graphics g, TreeMapNode<MODEL> node) {
    if (node.isLeaf()) {
      // TODO
      g.setColor(colorProvider.getColor(node.getUserObject()));
      g.fillRect(node.getX(), node.getY(), node.getWidth(), node.getHeight());
      g.setColor(Color.BLACK);
      g.drawRect(node.getX(), node.getY(), node.getWidth(), node.getHeight());
    } else {
      for (TreeMapNode<MODEL> child : node.getChildren()) {
        drawNode(g, child);
      }
    }
  }

  private void drawLabels(Graphics g, TreeMapNode<MODEL> node) {
    g.setFont(getFont());
    if (node.isLeaf()) {
      drawLabel(g, node);
    } else {
      for (TreeMapNode<MODEL> child : node.getChildren()) {
        drawLabel(g, child);
      }
    }
  }

  private void drawLabel(Graphics g, TreeMapNode node) {
    // TODO
    g.setColor(Color.BLACK);
    g.drawString(node.getUserObject().toString(), node.getX() + 5, node.getY() + 20);
  }

  private class MouseMotionHandler extends MouseMotionAdapter {
    @Override
    public void mouseMoved(MouseEvent e) {
      if (displayedRoot.children().hasMoreElements()) {
        TreeMapNode<MODEL> t = displayedRoot.getActiveLeaf(e.getX(), e.getY());
        if (t != null && !t.equals(getActiveLeaf())) {
          setActiveLeaf(t);
          repaint();
        }
      }
    }
  }
}
