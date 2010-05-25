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

package org.sonar.ide.ui.treemap.split;

import org.sonar.ide.ui.treemap.AbstractSplitStrategy;
import org.sonar.ide.ui.treemap.TreeMapNode;

import java.util.Iterator;
import java.util.List;

/**
 * @author Evgeny Mandrikov
 */
public class SplitByWeight extends AbstractSplitStrategy {
  protected void split(List<TreeMapNode> items, List<TreeMapNode> r1, List<TreeMapNode> r2) {
    final double weight = sumWeight(items);
    workOutWeight(items, r1, r2, weight);
  }

  protected void workOutWeight(final List<TreeMapNode> v1, final List<TreeMapNode> v2, final List<TreeMapNode> vClone, final double sumWeight) {
    double memWeight = 0.0;
    double elemWeight = 0.0;
    for (final Iterator<TreeMapNode> i = vClone.iterator(); i.hasNext();) {
      TreeMapNode tmn = i.next();
      elemWeight = tmn.getWeight();
      // if adding the current element pass the middle of total weight
      if (memWeight + elemWeight >= sumWeight / 2) {
        // we look at the finest split (the nearest of the middle of
        // weight)
        if (((sumWeight / 2) - memWeight) > ((memWeight + elemWeight) - (sumWeight / 2))) {
          // if it is after the add, we add the element to the first
          // Vector
          memWeight += elemWeight;
          v1.add(tmn);
        } else {
          // we must have at least 1 element in the first vector
          if (v1.isEmpty()) {
            v1.add(tmn);
          } else {
            // if it is before the add, we add the element to the
            // second Vector
            v2.add(tmn);
          }
        }
        // then we fill the second Vector qith the rest of elements
        while (i.hasNext()) {
          tmn = i.next();
          v2.add(tmn);
        }
      } else {
        // we add in the first vector while we don't reach the middle of
        // weight
        memWeight += elemWeight;
        v1.add(tmn);
      }
    }
  }
}
