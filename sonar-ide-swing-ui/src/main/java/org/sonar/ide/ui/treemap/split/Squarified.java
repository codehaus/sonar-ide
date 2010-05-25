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

import java.util.List;

/**
 * <a href="http://www.win.tue.nl/~vanwijk/stm.pdf">Squarified Treemaps (Mark Bruls, Kees Huizing, and Jarke J. van Wijk)</a>
 *
 * @author Evgeny Mandrikov
 */
public class Squarified extends AbstractSplitStrategy {
  @Override
  protected void split(List<TreeMapNode> items, List<TreeMapNode> r1, List<TreeMapNode> r2) {
    // TODO
  }
}
