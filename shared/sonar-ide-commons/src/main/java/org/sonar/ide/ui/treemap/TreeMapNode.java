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

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class TreeMapNode<MODEL> extends DefaultMutableTreeNode {
  private double weight;
  private int x;
  private int y;
  private int width;
  private int height;

  /**
   * Constructor for branch.
   */
  public TreeMapNode() {
    this.allowsChildren = true;
  }

  /**
   * Constructor for leaf.
   *
   * @param weight     weight of leaf
   * @param userObject object associated with this leaf
   */
  public TreeMapNode(double weight, MODEL userObject) {
    this.allowsChildren = false;
    this.weight = weight;
    this.userObject = userObject;
  }

  /**
   * Adds new child to this node.
   *
   * @param child new child
   */
  public void add(TreeMapNode child) {
    super.add(child);
    setWeight(weight + child.getWeight());
  }

  /**
   * Sets weight of this node and updates parents.
   *
   * @param weight new weight
   */
  public void setWeight(double weight) {
    if (weight < 0) {
      throw new UnsupportedOperationException("Negative weight not supported");
    }
    TreeMapNode parent = getParent();
    if (parent != null) {
      getParent().setWeight(parent.getWeight() - this.weight + weight);
    }
    this.weight = weight;
  }

  /**
   * Sets position and size.
   *
   * @param x      x-coordinate
   * @param y      y-coordinate
   * @param width  width
   * @param height height
   */
  public void setDimension(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Returns active leaf.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return active leaf
   */
  public TreeMapNode<MODEL> getActiveLeaf(int x, int y) {
    if (this.isLeaf()) {
      if ((getX() <= x) && (x <= getX() + getWidth())
          && (getY() <= y) && (y <= getY() + getHeight())) {
        return this;
      }
    } else {
      for (TreeMapNode<MODEL> node : getChildren()) {
        if ((node.getX() <= x) && (x <= node.getX() + node.getWidth())
            && (node.getY() <= y) && (y <= node.getY() + node.getHeight())) {
          return node.getActiveLeaf(x, y);
        }
      }
    }
    return null;
  }

  /**
   * Returns weight of this node.
   *
   * @return weight of this node
   */
  public double getWeight() {
    return weight;
  }

  @Override
  public TreeMapNode getParent() {
    return (TreeMapNode) super.getParent();
  }

  /**
   * Returns children of this node.
   *
   * @return children of this node
   */
  public List<TreeMapNode<MODEL>> getChildren() {
    //noinspection unchecked
    return children;
  }

  /**
   * Returns x-coordinate.
   *
   * @return x-coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Returns y-coordinate.
   *
   * @return y-coordinate
   */
  public int getY() {
    return y;
  }

  /**
   * Returns width.
   *
   * @return width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Returns height.
   *
   * @return height
   */
  public int getHeight() {
    return height;
  }

  @Override
  public MODEL getUserObject() {
    //noinspection unchecked
    return (MODEL) super.getUserObject();
  }
}
