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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public abstract class AbstractSplitStrategy {
  /**
   * Calculates positions for all elements starting from root.
   *
   * @param root root to calculate
   */
  public void calculatePositions(TreeMapNode root) {
    if (root == null) {
      return;
    }
    List<TreeMapNode> children = root.getChildren();
    if (!children.isEmpty()) {
      calculatePositionsRec(root.getX(), root.getY(), root.getWidth(), root.getHeight(), sumWeight(children), children);
    }
  }

  protected abstract void split(List<TreeMapNode> items, List<TreeMapNode> r1, List<TreeMapNode> r2);

  private void calculatePositionsRec(int x, int y, int width, int height, double weight, List<TreeMapNode> items) {
    if (items.isEmpty()) {
      return;
    }
    if (width * height < 20) {
      return;
    }
    if (width * height < items.size()) {
      return;
    }
    if (items.size() == 1) {
      TreeMapNode item = items.get(0);
      if (item.isLeaf()) {
        item.setDimension(x, y, width, height);
      } else {
        item.setDimension(x, y, width, height);
        calculatePositionsRec(x, y, width, height, weight, item.getChildren());
      }
    } else {
      List<TreeMapNode> i1 = new ArrayList<TreeMapNode>();
      List<TreeMapNode> i2 = new ArrayList<TreeMapNode>();
      split(i1, i2, items);
      double weight1 = sumWeight(i1);
      double weight2 = sumWeight(i2);
      int width1, width2, height1, height2, x2, y2;
      if (width > height) {
        width1 = (int) (width * weight1 / weight);
        width2 = width - width1;
        height1 = height;
        height2 = height;
        x2 = x + width1;
        y2 = y;
      } else {
        width1 = width;
        width2 = width;
        height1 = (int) (height * weight1 / weight);
        height2 = height - height1;
        x2 = x;
        y2 = y + height1;
      }
      calculatePositionsRec(x, y, width1, height1, weight1, i1);
      calculatePositionsRec(x2, y2, width2, height2, weight2, i2);
    }
  }

  protected double sumWeight(List<TreeMapNode> items) {
    double res = 0;
    for (TreeMapNode item : items) {
      res += item.getWeight();
    }
    return res;
  }
}
